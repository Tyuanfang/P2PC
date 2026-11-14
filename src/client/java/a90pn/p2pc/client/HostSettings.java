package a90pn.p2pc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameType;

/**
 * 客户端本地设置：写在 {@code config/p2pc-client.properties}。
 *
 * <p>记录房间默认值：端口、游戏模式、是否允许作弊、是否允许离线玩家、PvP、服务器描述。</p>
 */
public final class HostSettings {
    private static final String FILE_NAME = "p2pc-client.properties";
    private static final String KEY_ALLOW_OFFLINE = "allow-offline-players";
    private static final String KEY_LAST_PORT = "last-port";
    private static final String KEY_GAME_MODE = "game-mode";
    private static final String KEY_ALLOW_COMMANDS = "allow-commands";
    private static final String KEY_PVP = "pvp";
    private static final String KEY_MOTD = "motd";

    private static boolean loaded;
    private static boolean allowOfflinePlayers;
    private static int lastPort = 25565;
    private static GameType gameMode = GameType.SURVIVAL;
    private static boolean allowCommands;
    private static boolean pvp = true;
    private static String motd = "P2PC 房间";

    private HostSettings() {
    }

    public static boolean allowOfflinePlayers() {
        load();
        return allowOfflinePlayers;
    }

    public static void setAllowOfflinePlayers(boolean value) {
        load();
        allowOfflinePlayers = value;
        save();
    }

    public static int lastPort() {
        load();
        return lastPort;
    }

    public static void setLastPort(int value) {
        load();
        lastPort = value;
        save();
    }

    public static GameType gameMode() {
        load();
        return gameMode;
    }

    public static void setGameMode(GameType value) {
        load();
        gameMode = value;
        save();
    }

    public static boolean allowCommands() {
        load();
        return allowCommands;
    }

    public static void setAllowCommands(boolean value) {
        load();
        allowCommands = value;
        save();
    }

    public static boolean pvp() {
        load();
        return pvp;
    }

    public static void setPvp(boolean value) {
        load();
        pvp = value;
        save();
    }

    public static String motd() {
        load();
        return motd;
    }

    public static void setMotd(String value) {
        load();
        motd = value;
        save();
    }

    private static Path configPath() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.gameDirectory.toPath().resolve("config").resolve(FILE_NAME);
    }

    private static void load() {
        if (loaded) {
            return;
        }
        loaded = true;
        Path path = configPath();
        if (!Files.isRegularFile(path)) {
            return;
        }
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            properties.load(in);
        } catch (IOException ignored) {
            return;
        }
        allowOfflinePlayers = Boolean.parseBoolean(properties.getProperty(KEY_ALLOW_OFFLINE, "false"));
        try {
            lastPort = Integer.parseInt(properties.getProperty(KEY_LAST_PORT, "25565"));
        } catch (NumberFormatException ignored) {
            lastPort = 25565;
        }
        allowCommands = Boolean.parseBoolean(properties.getProperty(KEY_ALLOW_COMMANDS, "false"));
        pvp = Boolean.parseBoolean(properties.getProperty(KEY_PVP, "true"));
        motd = properties.getProperty(KEY_MOTD, "P2PC 房间");
        gameMode = parseGameType(properties.getProperty(KEY_GAME_MODE, GameType.SURVIVAL.getName()));
    }

    private static GameType parseGameType(String name) {
        for (GameType type : GameType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return GameType.SURVIVAL;
    }

    private static void save() {
        Properties properties = new Properties();
        properties.setProperty(KEY_ALLOW_OFFLINE, Boolean.toString(allowOfflinePlayers));
        properties.setProperty(KEY_LAST_PORT, Integer.toString(lastPort));
        properties.setProperty(KEY_GAME_MODE, gameMode.getName());
        properties.setProperty(KEY_ALLOW_COMMANDS, Boolean.toString(allowCommands));
        properties.setProperty(KEY_PVP, Boolean.toString(pvp));
        properties.setProperty(KEY_MOTD, motd);
        Path path = configPath();
        try {
            Files.createDirectories(path.getParent());
            try (OutputStream out = Files.newOutputStream(path)) {
                properties.store(out, "p2pc client settings");
            }
        } catch (IOException ignored) {
            // 设置写不进去不影响本次房间，下次启动退回默认值。
        }
    }
}
