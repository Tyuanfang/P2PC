package a90pn.p2pc.Sculk;

import a90pn.p2pc.ffi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SculkManager {

    Logger logger = LoggerFactory.getLogger("SculkManager");
    SculkStatues status;
    TunnelService tunnelService;
    JoinOptions joinOptions;
    HostOptions hostOptions;
    TunnelStatus tunnelStatus;
    boolean isWatching = false;
    boolean isClosing = false;
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
    public void join(JoinOptions joinOptions,ClientEvent event) {
        if (status == SculkStatues.JOIN) {
            logger.error("在尝试加入{}时发生错误：已经加入了{}尝试关闭连接",joinOptions.joinUri(),this.joinOptions.joinUri());
            status = SculkStatues.ERROR;
            close();
            return;
        }
        if(status == SculkStatues.HOST) {
            logger.error("在尝试加入{}时发生错误，已经是房主了，尝试复位",this.joinOptions.joinUri());
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
        tunnelService.startJoin(joinOptions);
        watch(event);
    }
    public void close(){
        if(isClosing) {
            return;
        }
        if (watcher != null) {
            watcher.close();
            watcher = null;
        }
        isWatching = false;

        if (tunnelService != null) {
            try {
                SculkClient.stop(tunnelService).get(2, TimeUnit.SECONDS);
            } catch (Exception e) {
                logger.warn("[p2pc] 停止隧道超时或异常", e);
            }
            tunnelService = null;
        }

        tunnelStatus = null;
        joinOptions = null;
        hostOptions = null;
        status = SculkStatues.IDLE;
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
                        logger.info("路径切换 relay={} rtt={}ms",
                                tunnelEvent.isRelay(), tunnelEvent.rttMs());
                        event.clientEvent(status, MassageKind.INFO,
                                "网络路径已切换", (tunnelEvent.isRelay() ? "中继" : "直连")
                                        + " rtt=" + tunnelEvent.rttMs() + "ms");
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
