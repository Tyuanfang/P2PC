package a90pn.p2pc.client.ui;

import a90pn.p2pc.P2PC;
import a90pn.p2pc.Sculk.SculkClient;
import a90pn.p2pc.Sculk.SculkStatues;
import a90pn.p2pc.client.HostSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;

/**
 * 创建房间：原版「对局域网开放」的完整替代品。
 *
 * <p>原版的三项（游戏模式、允许作弊、端口）都在，另外加上 P2PC 自己的：
 * 允许离线玩家、PvP、服务器描述（MOTD）。创建后把隧道挂到同一个端口上，</p>
 */
public class HostUI extends Screen {
    private static final int COLUMN = 150;
    private static final int GAP = 8;
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;

    private final Screen parent;

    private EditBox portBox;
    private EditBox motdBox;
    private GameType gameMode = GameType.SURVIVAL;
    private boolean allowCheats;
    private boolean offline;
    private boolean pvp = true;
    private String errorText = "";
    private long errorUntil;

    private int leftX;
    private int rightX;
    private int top;

    public HostUI(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.gameMode = HostSettings.gameMode();
        this.allowCheats = HostSettings.allowCommands();
        this.offline = HostSettings.allowOfflinePlayers();
        this.pvp = HostSettings.pvp();

        int total = COLUMN * 2 + GAP;
        this.leftX = this.width / 2 - total / 2;
        this.rightX = this.leftX + COLUMN + GAP;
        this.top = this.height / 2 - 96;

        // 第一行：游戏模式 / 允许作弊
        this.addRenderableWidget(CycleButton.builder(this::gameModeName)
                .withValues(GameType.SURVIVAL, GameType.CREATIVE, GameType.ADVENTURE, GameType.SPECTATOR)
                .withInitialValue(this.gameMode)
                .create(this.leftX, this.top, COLUMN, 20, Component.literal("游戏模式"),
                        (button, value) -> this.gameMode = value));

        this.addRenderableWidget(CycleButton.onOffBuilder(this.allowCheats)
                .create(this.rightX, this.top, COLUMN, 20, Component.literal("允许作弊"),
                        (button, value) -> this.allowCheats = value));

        // 第二行：端口 / 服务器描述
        this.portBox = new EditBox(this.font, this.leftX, this.top + 42, COLUMN, 20, Component.literal("端口"));
        this.portBox.setMaxLength(5);
        this.portBox.setValue(Integer.toString(HostSettings.lastPort()));
        this.portBox.setFilter(value -> value.isEmpty() || value.matches("\\d{0,5}"));
        this.addRenderableWidget(this.portBox);

        this.motdBox = new EditBox(this.font, this.rightX, this.top + 42, COLUMN, 20,
                Component.literal("服务器描述"));
        this.motdBox.setMaxLength(64);
        this.motdBox.setValue(HostSettings.motd());
        this.motdBox.setHint(Component.literal("服务器描述（MOTD）").withStyle(net.minecraft.ChatFormatting.GRAY));
        this.addRenderableWidget(this.motdBox);

        // 第三行：对其他玩家的设置
        this.addRenderableWidget(CycleButton.onOffBuilder(this.offline)
                .withTooltip(value -> net.minecraft.client.gui.components.Tooltip.create(value
                        ? Component.literal("关闭正版验证：没有正版账号的玩家也能进来")
                        : Component.literal("保持正版验证：只有正版账号能进来")))
                .create(this.leftX, this.top + 100, COLUMN, 20, Component.literal("允许离线玩家"),
                        (button, value) -> this.offline = value));

        this.addRenderableWidget(CycleButton.onOffBuilder(this.pvp)
                .create(this.rightX, this.top + 100, COLUMN, 20, Component.literal("启用 PvP"),
                        (button, value) -> this.pvp = value));

        // 底部：创建 / 返回
        this.addRenderableWidget(Button.builder(Component.literal("创建房间"), button -> this.startRoom())
                .bounds(this.width / 2 - 110, this.top + 140, 220, 20)
                .build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds(this.width / 2 - 110, this.top + 164, 220, 20)
                .build());
    }

    private Component gameModeName(GameType type) {
        return switch (type) {
            case SURVIVAL -> Component.literal("生存");
            case CREATIVE -> Component.literal("创造");
            case ADVENTURE -> Component.literal("冒险");
            case SPECTATOR -> Component.literal("旁观");
        };
    }

    private void showError(String message) {
        this.errorText = message;
        this.errorUntil = System.currentTimeMillis() + 2400L;
    }

    private void startRoom() {
        Minecraft minecraft = this.minecraft;
        if (minecraft == null) {
            return;
        }

        SculkStatues current = P2PC.getInstance().getSculkManager().getStatus();
        if (current == SculkStatues.JOIN) {
            showError("正在加入别人的世界，先断开再创建房间");
            return;
        }
        if (current == SculkStatues.HOST) {
            minecraft.setScreen(HostScreen.reopen(this.parent));
            return;
        }

        IntegratedServer server = minecraft.getSingleplayerServer();
        if (server == null) {
            showError("请先进入一个单人世界");
            return;
        }

        String text = this.portBox.getValue().trim();
        if (text.isEmpty()) {
            showError("请填写端口");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(text);
        } catch (NumberFormatException error) {
            showError("端口必须是数字");
            return;
        }
        if (port < MIN_PORT || port > MAX_PORT) {
            showError("端口范围是 " + MIN_PORT + "–" + MAX_PORT);
            return;
        }

        int actualPort = port;
        if (!server.isPublished()) {
            boolean published = server.publishServer(this.gameMode, this.allowCheats, port);
            if (!published) {
                showError("端口 " + port + " 开放失败，可能被占用");
                return;
            }
        } else {
            // 已经开放过局域网：沿用原端口（游戏模式/作弊以首次开放为准）。
            actualPort = server.getPort();
            this.portBox.setValue(Integer.toString(actualPort));
        }

        String motd = this.motdBox.getValue().trim();
        if (motd.isEmpty()) {
            motd = "P2PC 房间";
        }

        // 对应 MCWiFiPnP 的 Config.applyTo：正版验证 / PvP / MOTD。
        boolean previousOnlineMode = server.usesAuthentication();
        server.setUsesAuthentication(!this.offline);
        server.setPvpAllowed(this.pvp);
        server.setMotd(motd);

        HostSettings.setLastPort(actualPort);
        HostSettings.setGameMode(this.gameMode);
        HostSettings.setAllowCommands(this.allowCheats);
        HostSettings.setAllowOfflinePlayers(this.offline);
        HostSettings.setPvp(this.pvp);
        HostSettings.setMotd(motd);

        P2PC.getInstance().getSculkManager().host(
                SculkClient.hostOptions(actualPort, null, null, null, null, SculkClient.hostConfig(100L, null)),
                (status, kind, message, reason) -> HostScreen.pushEvent(kind, message, reason));

        minecraft.setScreen(new HostScreen(this.parent, actualPort, previousOnlineMode));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int titleY = Math.max(6, this.top - 28);
        guiGraphics.drawCenteredString(this.font, Component.literal("创建房间"), this.width / 2, titleY, 0xFFFFFF);

        // 原版「对局域网开放」的排版：标签在上、控件在下，两列对称。
        int label = 0xA0A0A0;
        guiGraphics.drawString(this.font, "游戏模式", this.leftX, this.top - 11, label);
        guiGraphics.drawString(this.font, "允许作弊", this.rightX, this.top - 11, label);
        guiGraphics.drawString(this.font, "端口", this.leftX, this.top + 31, label);
        guiGraphics.drawString(this.font, "服务器描述", this.rightX, this.top + 31, label);
        guiGraphics.drawString(this.font, "对其他玩家的设置", this.leftX, this.top + 78, 0xFFFFFF);
        guiGraphics.drawString(this.font, "允许离线玩家", this.leftX, this.top + 89, label);
        guiGraphics.drawString(this.font, "启用 PvP", this.rightX, this.top + 89, label);

        boolean isError = System.currentTimeMillis() < this.errorUntil;
        String hint = isError ? this.errorText : "创建后这里会生成邀请码，朋友用它加入";
        guiGraphics.drawCenteredString(this.font, Component.literal(hint),
                this.width / 2, this.top + 192, isError ? 0xFF5555 : 0x808080);
    }

    @Override
    public void onClose() {
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            minecraft.setScreen(this.parent);
        }
    }
}
