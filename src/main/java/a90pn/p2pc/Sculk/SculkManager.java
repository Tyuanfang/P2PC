package a90pn.p2pc.Sculk;

import a90pn.p2pc.ffi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * sculk 隧道的唯一管理器（进程内一个，挂在 {@link a90pn.p2pc.P2PC} 上）。
 *
 * <p>状态机：{@code AVAILABLE}（刚启动 / 刚复位）→ {@code JOIN} 或 {@code HOST}（隧道运行中）
 * → {@code close()} 回到 {@code IDLE}；{@code ERROR} 是「出过错、需要先 close() 复位」的中间态。
 *
 * <p>生命周期约定：
 * <ul>
 *   <li>{@link #watch(ClientEvent)} 由 join/host 内部启动，事件在守护线程上回调，
 *       UI 侧记得用 {@code Minecraft.execute} 切回客户端线程；</li>
 *   <li>{@link #close()} 幂等且同步：关订阅 → 停隧道 → 关服务对象 → 清空状态；</li>
 *   <li>连接被拒、断线、退出世界都会走到 close()，别假设「隧道一直活着」。</li>
 * </ul>
 */
public class SculkManager {

    Logger logger = LoggerFactory.getLogger("SculkManager");
    SculkStatues status;
    TunnelService tunnelService;
    JoinOptions joinOptions;
    HostOptions hostOptions;
    TunnelStatus tunnelStatus;
    boolean isWatching = false;
    boolean isClosing = false;
    /** 上一次上报的路径（relay/rtt），用于过滤核心重复的 PATH_CHANGED。 */
    Boolean lastPathRelay;
    long lastPathRtt;
    SculkClient.Watcher watcher;

    public SculkManager() {
        try {
            SculkClient.ensureLoaded();
            logger.info("[p2pc] sculk FFI 原生库已加载（MC 端口 {}，入站端口 {}）",
                    SculkClient.defaultMcPort(), SculkClient.defaultInletPort());
        } catch (Throwable error) {
            logger.error("[p2pc] sculk FFI 原生库加载失败", error);
        }
        tunnelService = SculkClient.newService();
        status = SculkStatues.AVAILABLE;
    }
    public SculkStatues getStatus() {
        return status;
    }

    public TunnelStatus getTunnelStatus() {
        return tunnelStatus;
    }

    public void join(JoinOptions joinOptions,ClientEvent event) {
        if (status == SculkStatues.JOIN) {
            logger.error("在尝试加入{}时发生错误：已经加入了{}，尝试关闭连接", joinOptions.joinUri(),
                    this.joinOptions == null ? "(未知)" : this.joinOptions.joinUri());
            status = SculkStatues.ERROR;
            close();
            return;
        }
        if(status == SculkStatues.HOST) {
            logger.error("在尝试加入{}时发生错误，已经是房主了，尝试复位", joinOptions.joinUri());
            status = SculkStatues.ERROR;
            close();
            return;
        }
        if(status == SculkStatues.ERROR) {
            return;
        }
        if(status == SculkStatues.IDLE) {
            tunnelService = SculkClient.newService();
        }
        status = SculkStatues.JOIN;
        this.joinOptions = joinOptions;
        // startJoin 失败（邀请码过期、端点不可用……）时不会有事件回来，
        // 这里主动收尾并回报错误，免得界面只能干等 45 秒超时。
        tunnelService.startJoin(joinOptions).whenComplete((ignored, error) -> {
            if (error != null) {
                reportStartFailure(event, "加入失败", error);
            }
        });
        watch(event);
    }
    public void host(HostOptions hostOptions,ClientEvent event) {
        if (status == SculkStatues.JOIN) {
            logger.error("在尝试成为房主时发生错误：处于加入状态");
            return;
        }
        if(status == SculkStatues.HOST) {
            logger.error("已经是房主了");
            return;
        }
        if(status == SculkStatues.ERROR) {
            return;
        }
        if(status == SculkStatues.IDLE) {
            tunnelService = SculkClient.newService();
        }
        status = SculkStatues.HOST;
        // 同 join：启动失败要自己收尾，别让界面停在「正在创建隧道…」。
        tunnelService.startHost(hostOptions).whenComplete((ignored, error) -> {
            if (error != null) {
                reportStartFailure(event, "创建房间失败", error);
            }
        });
        watch(event);
    }

    /**
     * startJoin / startHost 的 future 失败时收尾：复位状态、通知界面、清理隧道。
     *
     * <p>清理放到守护线程里做——{@code close()} 会 {@code stop().get(2s)}，
     * 而这里已经在 FFI 的执行线程上，直接调用可能把线程卡住两秒。
     */
    private void reportStartFailure(ClientEvent event, String message, Throwable error) {
        Throwable cause = error instanceof java.util.concurrent.CompletionException && error.getCause() != null
                ? error.getCause()
                : error;
        logger.error("{}：{}", message, cause.toString());

        this.status = SculkStatues.AVAILABLE;
        try {
            event.clientEvent(this.status, MassageKind.ERROR, message, cause.getMessage());
        } catch (RuntimeException ignored) {
            // 回调自己出错不影响清理
        }

        Thread cleanup = new Thread(this::close, "p2pc-tunnel-cleanup");
        cleanup.setDaemon(true);
        cleanup.start();
    }
    public synchronized void close(){
        if(isClosing) {
            return;
        }
        isClosing = true;
        try {
            if (watcher != null) {
                watcher.close();
                watcher = null;
            }
            isWatching = false;

            if (tunnelService != null) {
                // 先停隧道（最多等 2 秒），再关掉服务对象本身；
                // TunnelService 是 UniFFI 生成的原生对象，不 close() 就要等 GC 才释放句柄。
                TunnelService service = tunnelService;
                tunnelService = null;
                try {
                    SculkClient.stop(service).get(2, TimeUnit.SECONDS);
                } catch (Exception e) {
                    logger.warn("[p2pc] 停止隧道超时或异常", e);
                }
                try {
                    service.close();
                } catch (RuntimeException ignored) {
                    // 已经在 Rust 侧销毁/重复关闭，忽略
                }
            }

            tunnelStatus = null;
            joinOptions = null;
            hostOptions = null;
            lastPathRelay = null;
            lastPathRtt = 0L;
            status = SculkStatues.IDLE;
        } finally {
            isClosing = false;
        }
    }
    private void watch(ClientEvent event) {
        if (isWatching) return;
        isWatching = true;
       this.watcher = SculkClient.watch(tunnelService,tunnelUpdate ->{
            if(!tunnelUpdate.isEvent()){
                this.tunnelStatus = tunnelUpdate.status();
            }
            else{
                TunnelEvent tunnelEvent = tunnelUpdate.event();
                switch (tunnelEvent.kind()) {
                    case AUTH_FAILED -> {
                        logger.error("鉴权失败：{}", tunnelEvent.message());
                        event.clientEvent(status, MassageKind.ERROR,
                                "鉴权失败", tunnelEvent.message());
                        status = SculkStatues.AVAILABLE;
                    }
                    case PLAYER_JOINED -> {
                        logger.info("玩家加入 {}", tunnelEvent.playerId());
                        event.clientEvent(status, MassageKind.INFO,
                                "玩家加入", tunnelEvent.playerId());
                    }
                    case PLAYER_LEFT -> {
                        logger.info("玩家离开 {} 原因 {}", tunnelEvent.playerId(), tunnelEvent.reason());
                        event.clientEvent(status, MassageKind.INFO,
                                "玩家离开", tunnelEvent.playerId()
                                        + (tunnelEvent.reason() != null ? " (" + tunnelEvent.reason() + ")" : ""));
                    }
                    case PLAYER_REJECTED -> {
                        logger.warn("玩家被拒 {} 原因 {}", tunnelEvent.playerId(), tunnelEvent.reason());
                        event.clientEvent(status, MassageKind.WARNING,
                                "玩家被拒绝", tunnelEvent.playerId()
                                        + (tunnelEvent.reason() != null ? " (" + tunnelEvent.reason() + ")" : ""));
                    }
                    case CONNECTED -> {
                        logger.info("已连接 {} relay={} rtt={}ms",
                                tunnelEvent.remoteId(), tunnelEvent.isRelay(), tunnelEvent.rttMs());
                        event.clientEvent(status, MassageKind.INFO,
                                "已连接", (tunnelEvent.isRelay() ? "中继" : "直连")
                                        + " rtt=" + tunnelEvent.rttMs() + "ms");

                    }
                    case DISCONNECTED -> {
                        logger.warn("连接断开 {} 原因 {}", tunnelEvent.remoteId(), tunnelEvent.reason());
                        event.clientEvent(status, MassageKind.WARNING,
                                "连接断开", tunnelEvent.reason());
                        close();
                    }
                    case PATH_CHANGED -> {
                        boolean relay = tunnelEvent.isRelay();
                        long rtt = tunnelEvent.rttMs();
                        boolean changed = this.lastPathRelay == null
                                || this.lastPathRelay != relay
                                || this.lastPathRtt != rtt;
                        if (changed) {
                            this.lastPathRelay = relay;
                            this.lastPathRtt = rtt;
                            logger.info("路径切换 relay={} rtt={}ms", relay, rtt);
                            event.clientEvent(status, MassageKind.INFO,
                                    "网络路径已切换", (relay ? "中继" : "直连")
                                            + " rtt=" + rtt + "ms");
                        }
                    }
                    case RECONNECTING -> {
                        Long wait = tunnelEvent.retryInMs();
                        logger.warn("重连中 attempt={} 下次{}ms后", tunnelEvent.attempt(), wait);
                        event.clientEvent(status, MassageKind.WARNING,
                                "正在重连",
                                "第 " + tunnelEvent.attempt() + " 次"
                                        + (wait != null ? "，" + wait + "ms 后重试" : ""));
                    }
                    case RECONNECTED -> {
                        logger.info("重连成功 {}", tunnelEvent.remoteId());
                        event.clientEvent(status, MassageKind.INFO,
                                "重连成功", tunnelEvent.remoteId());
                    }
                    case TOKEN_ROTATED -> {
                        logger.info("访问令牌已轮换");
                        event.clientEvent(status, MassageKind.INFO,
                                "访问令牌已更新", "请重新分享房间");
                    }
                    case TOKEN_ROTATION_FAILED -> {
                        logger.error("令牌轮换失败：{}", tunnelEvent.message());
                        event.clientEvent(status, MassageKind.ERROR,
                                "令牌轮换失败", tunnelEvent.message());
                    }
                    case ERROR -> {
                        logger.error("隧道错误[{}]：{}", tunnelEvent.category(), tunnelEvent.message());
                        event.clientEvent(status, MassageKind.ERROR,
                                "隧道错误", tunnelEvent.message());
                        close();
                    }
                }
            }
        });
    }
}
