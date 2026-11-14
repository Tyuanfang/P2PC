package a90pn.p2pc.client;

import a90pn.p2pc.P2PC;
import a90pn.p2pc.Sculk.SculkClient;
import a90pn.p2pc.Sculk.SculkManager;
import a90pn.p2pc.Sculk.SculkStatues;
import a90pn.p2pc.ffi.LanBroadcaster;
import a90pn.p2pc.ffi.ScuLkException;
import a90pn.p2pc.ffi.TunnelPhase;
import a90pn.p2pc.ffi.TunnelState;
import a90pn.p2pc.ffi.TunnelStatus;
import net.minecraft.client.Minecraft;

/**
 * 把「加入别人世界」时本机的隧道入口，用原版 LAN 公告的形式广播出去。
 *
 * <p>Minecraft 客户端连接局域网世界时，连的是公告数据报的来源 IP；这个广播只发给
 * {@code 127.0.0.1:4445}，所以多人游戏列表里会多出一条指向本机隧道入口的「局域网世界」，
 * 点进去走的就是 P2PC 隧道本身——和只输邀请码自动直连相比，多一条「像局域网一样发现」的路。</p>
 *
 * <p>和别的自定义局域网模组共存：公告只是往同一个组播端口再发一条数据，各模组各写各的
 * 世界名，列表里会各出现一条，互不覆盖。</p>
 */
public final class LanAnnouncer {
    /** 公告里显示的世界名（原版限制 64 字符）。 */
    private static final String DEFAULT_NAME = "P2PC 房间";

    private static LanBroadcaster broadcaster;
    private static int announcedPort = -1;
    private static String announcedName;

    private LanAnnouncer() {
    }

    /**
     * 每客户端 tick 调一次：状态是「加入中/已加入」且隧道已建立时保持广播，否则停掉。
     */
    public static void tick() {
        SculkManager manager = manager();
        if (manager == null || manager.getStatus() != SculkStatues.JOIN) {
            stop();
            return;
        }
        if (!HostSettings.lanAnnounce()) {
            stop();
            return;
        }
        int port = localInletPort(manager.getTunnelStatus());
        if (port <= 0) {
            // 还在打洞阶段，等隧道真的通了再广播
            stop();
            return;
        }
        String name = announceName();
        if (broadcaster != null && port == announcedPort && name.equals(announcedName)) {
            return;
        }
        stop();
        try {
            broadcaster = SculkClient.lanBroadcasterStart(name, port);
            announcedPort = port;
            announcedName = name;
        } catch (ScuLkException error) {
            P2PC.LOGGER.warn("[p2pc] 局域网公告启动失败：{}", error.getMessage());
        }
    }

    /** 停止广播（断开、退出世界、关闭功能时都会走到）。 */
    public static void stop() {
        LanBroadcaster current = broadcaster;
        broadcaster = null;
        announcedPort = -1;
        announcedName = null;
        if (current == null) {
            return;
        }
        try {
            current.stop();
        } catch (ScuLkException ignored) {
            // 广播线程已经收掉时 stop 会抛，忽略即可
        } finally {
            current.close();
        }
    }

    /**
     * 从隧道状态里取本机入口端口，比如 {@code 127.0.0.1:30000} → 30000。
     */
    private static int localInletPort(TunnelStatus status) {
        if (status == null) {
            return -1;
        }
        TunnelState state = status.state();
        if (state == null) {
            return -1;
        }
        TunnelPhase phase = state.phase();
        if (phase != TunnelPhase.ACTIVE && phase != TunnelPhase.STOPPING) {
            return -1;
        }
        String localAddress = state.localAddr();
        if (localAddress == null) {
            return -1;
        }
        int colon = localAddress.lastIndexOf(':');
        if (colon < 0) {
            return -1;
        }
        try {
            return Integer.parseInt(localAddress.substring(colon + 1));
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    /**
     * 公告用的世界名：优先用设置的 MOTD，没有就退回默认。
     */
    private static String announceName() {
        String motd = HostSettings.motd();
        if (motd == null || motd.isBlank()) {
            return DEFAULT_NAME;
        }
        // 原版局域网世界名有长度上限，超了会被截断，这里先自己截
        return motd.length() > 64 ? motd.substring(0, 64) : motd;
    }

    private static SculkManager manager() {
        P2PC mod = P2PC.getInstance();
        return mod == null ? null : mod.getSculkManager();
    }
}
