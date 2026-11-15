package a90pn.p2pc.compat;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameType;

/**
 * 本地世界「对局域网开放」的兼容封装。
 *
 * <p>26.3 起 {@code MinecraftServer#publishServer} 的第一个参数从 {@link GameType}
 * 换成了 {@code MinecraftServer.MultiplayerScope}（OFF/LAN 两档，代表允许哪些玩家进来），
 * 游戏模式改成在发布前用 {@code setDefaultGameType} 设置。</p>
 */
public final class Ports {
    private Ports() {}

    /**
     * 把本地单人世界发布到指定端口。
     *
     * @return 是否发布成功
     */
    public static boolean publishServer(
            MinecraftServer server, GameType gameType, boolean allowCommands, int port) {
        //? if <26.3 {
        return server.publishServer(gameType, allowCommands, port);
        //?} else {
        /*server.setDefaultGameType(gameType);
        return server.publishServer(MinecraftServer.MultiplayerScope.LAN, allowCommands, port);
        *///?}
    }
}
