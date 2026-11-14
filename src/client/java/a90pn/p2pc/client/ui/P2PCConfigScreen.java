package a90pn.p2pc.client.ui;

import a90pn.p2pc.client.HostSettings;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;

/**
 * Mod Menu 里的模组设置页：改的是「下次开房间的默认值」和几个联机开关。
 *
 * <p>所有改动即时写入 {@code config/p2pc-client.properties}，不需要点保存；
 * 正在进行的房间不受影响，下次打开设置页才生效。</p>
 */
public class P2PCConfigScreen extends Screen {
    private static final int WIDGET_WIDTH = 220;
    private static final int ROW = 24;

    private final Screen parent;
    private int top;
    private EditBox portBox;
    private EditBox motdBox;

    public P2PCConfigScreen(Screen parent) {
        super(Component.literal("P2PC 设置"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - WIDGET_WIDTH / 2;
        this.top = this.height / 2 - 92;
        int y = this.top;

        // 默认端口
        this.portBox = new EditBox(this.font, x, y, WIDGET_WIDTH, 20, Component.literal("默认端口"));
        this.portBox.setValue(Integer.toString(HostSettings.lastPort()));
        this.portBox.setMaxLength(5);
        this.portBox.setResponder(value -> {
            if (value.isEmpty()) {
                return;
            }
            try {
                int port = Integer.parseInt(value);
                if (port >= 1024 && port <= 65535) {
                    HostSettings.setLastPort(port);
                }
            } catch (NumberFormatException ignored) {
                // 输入还没成形，忽略
            }
        });
        y += ROW;
        this.addRenderableWidget(this.portBox);

        // 游戏模式
        y += ROW;
        this.addRenderableWidget(CycleButton.<GameType>builder(type -> Component.literal(displayName(type)))
                .withValues(GameType.SURVIVAL, GameType.CREATIVE, GameType.ADVENTURE, GameType.SPECTATOR)
                .withInitialValue(HostSettings.gameMode())
                .create(x, y, WIDGET_WIDTH, 20, Component.literal("游戏模式"),
                        (button, value) -> HostSettings.setGameMode(value)));

        // 允许作弊
        y += ROW;
        this.addRenderableWidget(CycleButton.onOffBuilder(HostSettings.allowCommands())
                .create(x, y, WIDGET_WIDTH, 20, Component.literal("允许作弊"),
                        (button, value) -> HostSettings.setAllowCommands(value)));

        // 允许离线玩家
        y += ROW;
        this.addRenderableWidget(CycleButton.onOffBuilder(HostSettings.allowOfflinePlayers())
                .create(x, y, WIDGET_WIDTH, 20, Component.literal("允许离线玩家"),
                        (button, value) -> HostSettings.setAllowOfflinePlayers(value)));

        // PvP
        y += ROW;
        this.addRenderableWidget(CycleButton.onOffBuilder(HostSettings.pvp())
                .create(x, y, WIDGET_WIDTH, 20, Component.literal("启用 PvP"),
                        (button, value) -> HostSettings.setPvp(value)));

        // 服务器描述 / 局域网公告名
        y += ROW;
        this.motdBox = new EditBox(this.font, x, y, WIDGET_WIDTH, 20, Component.literal("服务器描述"));
        this.motdBox.setValue(HostSettings.motd());
        this.motdBox.setMaxLength(64);
        this.motdBox.setResponder(HostSettings::setMotd);
        this.addRenderableWidget(this.motdBox);

        // 局域网公告
        y += ROW;
        this.addRenderableWidget(CycleButton.onOffBuilder(HostSettings.lanAnnounce())
                .create(x, y, WIDGET_WIDTH, 20, Component.literal("在局域网列表公告"),
                        (button, value) -> HostSettings.setLanAnnounce(value)));

        // 完成
        y += ROW + 8;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                .bounds(x, y, WIDGET_WIDTH, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, Math.max(8, this.top - 34), 0xFFFFFF);
        int x = this.width / 2 - WIDGET_WIDTH / 2;
        // 每行左边的小标签
        guiGraphics.drawString(this.font, "默认端口", x, this.top - 10, 0xA0A0A0);
        guiGraphics.drawCenteredString(this.font,
                Component.literal("加入别人的世界时，可把本机隧道入口公告到局域网列表"),
                this.width / 2, this.top + ROW * 6 + 2, 0x808080);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    private static String displayName(GameType type) {
        return switch (type) {
            case SURVIVAL -> "生存";
            case CREATIVE -> "创造";
            case ADVENTURE -> "冒险";
            case SPECTATOR -> "旁观";
        };
    }
}
