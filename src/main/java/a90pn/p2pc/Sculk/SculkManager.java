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
 *
 * <p>线程模型（改这里之前先读）：
 * <ul>
 *   <li>{@link #status} / {@link #tunnelStatus} 会被事件线程写、客户端线程读，所以是 volatile；</li>
 *   <li>事件回调（{@code clientEvent}）是界面代码，可能抛异常，统一走 {@link #emit}
 *       吞掉并记日志——否则一次 UI 异常就会把事件泵线程打死；</li>
 *   <li>不要在事件线程里直接 {@code close()}：停隧道最多阻塞 2 秒，而且订阅端自己
 *       close 时 interrupt 会打断当前线程。收尾统一用 {@link #closeAsync()}。</li>
 * </ul>
 */
public class SculkManager {

    Logger logger = LoggerFactory.getLogger("SculkManager");
    volatile SculkStatues status;
    TunnelService tunnelService;
    volatile JoinOptions joinOptions;
    volatile HostOptions hostOptions;
    volatile TunnelStatus tunnelStatus;
    volatile boolean isWatching = false;
    volatile boolean isClosing = false;
    /** 上一次上报的路径（relay/rtt），用于过滤核心重复的 PATH_CHANGED。 */
    Boolean lastPathRelay;
    long lastPathRtt;
    volatile SculkClient.Watcher watcher;

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
            closeAsync();
            return;
        }
        if(status == SculkStatues.HOST) {
            logger.error("在尝试加入{}时发生错误，已经是房主了，尝试复位", joinOptions.joinUri());
            status = SculkStatues.ERROR;
            closeAsync();
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
        this.hostOptions = hostOptions;
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
        emit(event, MassageKind.ERROR, message, cause.getMessage());
        closeAsync();
    }

    /** 在后台线程上跑 {@link #close()}，避免在事件线程/客户端线程上阻塞。 */
    private void closeAsync() {
        Thread cleanup = new Thread(this::close, "p2pc-tunnel-cleanup");
        cleanup.setDaemon(true);
        cleanup.start();
    }

    /**
     * 向界面回调一次事件；界面代码抛异常只记录，绝不让事件泵线程挂掉。
     *
     * <p>注意先读一次 {@link #status}：回调里可能顺手关闭隧道（把状态改成 IDLE），
     * 用快照保证上报的状态是「事件发生那一刻」的状态。
     */
    private void emit(ClientEvent event, MassageKind kind, String message, String reason) {
        try {
            event.clientEvent(status, kind, message, reason);
        } catch (RuntimeException error) {
            logger.warn("[p2pc] 事件回调抛异常（已忽略）", error);
        }
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
        try {
            this.watcher = SculkClient.watch(tunnelService, tunnelUpdate -> {
                try {
                    handleUpdate(tunnelUpdate, event);
                } catch (RuntimeException error) {
                    // 事件处理/界面回调出错不能把泵线程带走：记日志、复位状态、收隧道。
                    logger.error("[p2pc] 处理隧道事件时异常", error);
                    status = SculkStatues.AVAILABLE;
                    closeAsync();
                }
            });
        } catch (RuntimeException error) {
            // 订阅都没建起来（原生库/服务对象异常）：复位标记，下次还能再试。
            isWatching = false;
            logger.error("[p2pc] 启动事件订阅失败", error);
            return;
        }
        if (this.watcher == null) {
            isWatching = false;
        }
    }

    /** 把一条 {@link TunnelUpdate} 翻译成界面事件。 */
    private void handleUpdate(TunnelUpdate tunnelUpdate, ClientEvent event) {
        if (tunnelUpdate == null) {
            return;
        }
        if (!tunnelUpdate.isEvent()) {
            this.tunnelStatus = tunnelUpdate.status();
            return;
        }
        TunnelEvent tunnelEvent = tunnelUpdate.event();
        if (tunnelEvent == null || tunnelEvent.kind() == null) {
            return;
        }
        switch (tunnelEvent.kind()) {
            case AUTH_FAILED -> {
                logger.error("鉴权失败：{}", tunnelEvent.message());
                emit(event, MassageKind.ERROR, "鉴权失败", tunnelEvent.message());
                // 鉴权失败后隧道已经不可用，复位到「可重试」并回收；否则会一直停在 JOIN 状态。
                status = SculkStatues.AVAILABLE;
                closeAsync();
            }
            case PLAYER_JOINED -> {
                logger.info("玩家加入 {}", tunnelEvent.playerId());
                emit(event, MassageKind.INFO, "玩家加入", tunnelEvent.playerId());
            }
            case PLAYER_LEFT -> {
                logger.info("玩家离开 {} 原因 {}", tunnelEvent.playerId(), tunnelEvent.reason());
                emit(event, MassageKind.INFO, "玩家离开", tunnelEvent.playerId()
                        + (tunnelEvent.reason() != null ? " (" + tunnelEvent.reason() + ")" : ""));
            }
            case PLAYER_REJECTED -> {
                logger.warn("玩家被拒 {} 原因 {}", tunnelEvent.playerId(), tunnelEvent.reason());
                emit(event, MassageKind.WARNING, "玩家被拒绝", tunnelEvent.playerId()
                        + (tunnelEvent.reason() != null ? " (" + tunnelEvent.reason() + ")" : ""));
            }
            case CONNECTED -> {
                logger.info("已连接 {} relay={} rtt={}ms",
                        tunnelEvent.remoteId(), tunnelEvent.isRelay(), tunnelEvent.rttMs());
                emit(event, MassageKind.INFO, "已连接", (tunnelEvent.isRelay() ? "中继" : "直连")
                        + " rtt=" + tunnelEvent.rttMs() + "ms");
            }
            case DISCONNECTED -> {
                logger.warn("连接断开 {} 原因 {}", tunnelEvent.remoteId(), tunnelEvent.reason());
                emit(event, MassageKind.WARNING, "连接断开", tunnelEvent.reason());
                closeAsync();
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
                    emit(event, MassageKind.INFO, "网络路径已切换", (relay ? "中继" : "直连")
                            + " rtt=" + rtt + "ms");
                }
            }
            case RECONNECTING -> {
                Long wait = tunnelEvent.retryInMs();
                logger.warn("重连中 attempt={} 下次{}ms后", tunnelEvent.attempt(), wait);
                emit(event, MassageKind.WARNING, "正在重连", "第 " + tunnelEvent.attempt() + " 次"
                        + (wait != null ? "，" + wait + "ms 后重试" : ""));
            }
            case RECONNECTED -> {
                logger.info("重连成功 {}", tunnelEvent.remoteId());
                emit(event, MassageKind.INFO, "重连成功", tunnelEvent.remoteId());
            }
            case TOKEN_ROTATED -> {
                logger.info("访问令牌已轮换");
                emit(event, MassageKind.INFO, "访问令牌已更新", "请重新分享房间");
            }
            case TOKEN_ROTATION_FAILED -> {
                logger.error("令牌轮换失败：{}", tunnelEvent.message());
                emit(event, MassageKind.ERROR, "令牌轮换失败", tunnelEvent.message());
            }
            case ERROR -> {
                logger.error("隧道错误[{}]：{}", tunnelEvent.category(), tunnelEvent.message());
                emit(event, MassageKind.ERROR, "隧道错误", tunnelEvent.message());
                closeAsync();
            }
        }
    }
}
