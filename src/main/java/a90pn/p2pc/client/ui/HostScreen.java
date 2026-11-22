package a90pn.p2pc.client.ui;

import a90pn.p2pc.client.UiText;

import a90pn.p2pc.P2PC;
import a90pn.p2pc.Sculk.MassageKind;
import a90pn.p2pc.Sculk.SculkClient;
import a90pn.p2pc.Sculk.SculkManager;
import a90pn.p2pc.compat.BaseScreen;
import a90pn.p2pc.compat.Gfx;
import a90pn.p2pc.ffi.ConnectionSnapshot;
import a90pn.p2pc.ffi.TunnelStatus;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * 房间运行页：显示邀请码、在线人数，停止后恢复原来的正版验证设置。
 */
public class HostScreen extends BaseScreen {
    private static final int WIDGET_WIDTH = 220;

    private static final Component TITLE = Component.literal("房间已开启");
    private static final Component PUBLISH_WARNING = Component.literal("不要将链接随意发布在网络上");
    private static final Component COPY_TIP = Component.literal("复制邀请码发给朋友，对方在标题界面点末影珍珠按钮加入");
    private static final Component HELP = Component.literal(UiText.HELP_LINE);

    private static volatile HostScreen active;
    /**
     * 记忆「开放局域网之前」的正版验证设置。
     *
     * <p>房间页可能被关掉再打开（暂停菜单里的「管理房间」），而 Minecraft 没有 API 能查
     * 出原始值，所以由创建房间的 {@link HostUI} 记一次，重开时沿用。</p>
     */
    private static volatile Boolean rememberedOnlineMode;

    private final Screen parent;
    private final int port;
    private final boolean previousOnlineMode;

    private EditBox inviteBox;
    private Component statusText = Component.literal("正在创建隧道…");
    private int statusColor = 0xA0A0A0;
    private Component playersText = Component.literal("当前在线：0");
    private int players;
    private boolean stopped;
    /** 内容区顶部（竖排布局的基准线）。 */
    private int top;
    private Component portText = Component.literal("端口 0");

    public HostScreen(Screen parent, int port, boolean previousOnlineMode) {
        super(Component.literal("房间"));
        this.parent = parent;
        this.port = port;
        this.previousOnlineMode = previousOnlineMode;
        rememberedOnlineMode = previousOnlineMode;
    }

    public static HostScreen reopen(Screen parent) {
        Minecraft minecraft = Minecraft.getInstance();
        IntegratedServer server = minecraft.getSingleplayerServer();
        int port = server != null ? server.getPort() : 0;
        Boolean remembered = rememberedOnlineMode;
        return new HostScreen(parent, port, remembered == null ? true : remembered);
    }

    /** 由 HostUI 传给 SculkManager 的回调转发到这里（事件在隧道线程上）。 */
    public static void pushEvent(MassageKind kind, String message, String reason) {
        Minecraft.getInstance().execute(() -> {
            HostScreen screen = active;
            if (screen != null) {
                screen.onEvent(kind, message, reason);
            }
        });
    }

    private void onEvent(MassageKind kind, String message, String reason) {
        String detail = UiText.detail(message, reason);
        switch (kind) {
            case ERROR -> {
                this.statusText = Component.literal(detail);
                this.statusColor = 0xFF5555;
            }
            case WARNING -> {
                this.statusText = Component.literal(detail);
                this.statusColor = 0xFFAA55;
            }
            case INFO -> {
                this.statusText = Component.literal(detail);
                this.statusColor = 0xA0A0A0;
            }
        }
    }

    @Override
    protected void init() {
        active = this;

        int x = this.width / 2 - WIDGET_WIDTH / 2;
        this.top = this.height / 2 - 90;
        this.portText = Component.literal("端口 " + this.port);

        this.inviteBox = new EditBox(this.font, x, this.top, WIDGET_WIDTH, 20, Component.literal("邀请码"));
        this.inviteBox.setMaxLength(256);
        this.inviteBox.setEditable(false);
        this.inviteBox.setHint(Component.literal("正在建立隧道…").withStyle(ChatFormatting.GRAY));
        this.addRenderableWidget(this.inviteBox);

        this.addRenderableWidget(Button.builder(Component.literal("复制邀请码"), button -> this.copyInvite())
                .bounds(x, this.top + 24, WIDGET_WIDTH, 20)
                .build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds(x, this.top + 96, WIDGET_WIDTH, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("停止房间"), button -> this.stopRoom())
                .bounds(x, this.top + 120, WIDGET_WIDTH, 20)
                .build());

        SculkManager manager = manager();
        if (manager != null) {
            TunnelStatus status = manager.getTunnelStatus();
            if (status != null) {
                String uri = SculkClient.shareUri(status);
                if (uri != null) {
                    this.inviteBox.setValue(uri);
                }
            }
        }
    }

    /** 管理器（模组没初始化/没传进来时返回 null，界面照常显示，只是不刷新）。 */
    private static SculkManager manager() {
        P2PC mod = P2PC.getInstance();
        return mod == null ? null : mod.getSculkManager();
    }

    private void copyInvite() {
        Minecraft minecraft = this.minecraft;
        if (minecraft == null || this.inviteBox == null) {
            return;
        }
        String uri = this.inviteBox.getValue();
        if (!uri.isEmpty()) {
            minecraft.keyboardHandler.setClipboard(uri);
            this.statusText = Component.literal("邀请码已复制到剪贴板");
            this.statusColor = 0x55FF55;
        }
    }

    private void stopRoom() {
        if (this.stopped) {
            return;
        }
        this.stopped = true;

        // 收隧道最多会阻塞两秒（stop().get(2s)），不能卡在客户端主线程上。
        Thread cleanup = new Thread(() -> {
            SculkManager manager = manager();
            if (manager != null) {
                manager.close();
            }
        }, "p2pc-host-stop");
        cleanup.setDaemon(true);
        cleanup.start();

        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            IntegratedServer server = minecraft.getSingleplayerServer();
            if (server != null) {
                // 恢复开放前的正版验证设置。
                server.setUsesAuthentication(this.previousOnlineMode);
            }
            a90pn.p2pc.compat.ScreenSwap.to(minecraft, this.parent);
        }
    }

    @Override
    public void tick() {
        super.tick();
        SculkManager manager = manager();
        if (manager == null || this.stopped) {
            return;
        }
        TunnelStatus status = manager.getTunnelStatus();
        if (status == null) {
            return;
        }
        String uri = SculkClient.shareUri(status);
        if (uri != null && this.inviteBox != null && !uri.equals(this.inviteBox.getValue())) {
            this.inviteBox.setValue(uri);
            if (this.statusText.getString().equals("正在创建隧道…")) {
                this.statusText = Component.literal("邀请码已就绪");
                this.statusColor = 0x55FF55;
            }
        }
        int alive = 0;
        List<ConnectionSnapshot> connections = status.connections();
        if (connections != null) {
            for (ConnectionSnapshot snapshot : connections) {
                if (snapshot != null && snapshot.alive()) {
                    alive++;
                }
            }
        }
        if (alive != this.players) {
            this.players = alive;
            this.playersText = Component.literal("当前在线：" + alive);
        }
    }

    @Override
    protected void drawContents(Object guiGraphics, int mouseX, int mouseY, float partialTick) {
        int titleY = Math.max(8, this.top - 30);
        Gfx.centeredText(guiGraphics, this.font, TITLE, this.width / 2, titleY, 0xFFFFFF);
        Gfx.centeredText(guiGraphics, this.font, this.portText, this.width / 2, this.top - 14, 0x9C9C9C);
        Gfx.centeredText(guiGraphics, this.font, PUBLISH_WARNING, this.width / 2, this.top + 48, 0xFF5555);
        Gfx.centeredText(guiGraphics, this.font, this.statusText, this.width / 2, this.top + 62, this.statusColor);
        Gfx.centeredText(guiGraphics, this.font, this.playersText, this.width / 2, this.top + 74, 0x9C9C9C);
        Gfx.centeredText(guiGraphics, this.font, COPY_TIP, this.width / 2, this.top + 132, 0x707070);
        Gfx.centeredText(guiGraphics, this.font, HELP, this.width / 2, this.top + 146, 0x707070);
    }

    @Override
    public void onClose() {
        active = null;
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            a90pn.p2pc.compat.ScreenSwap.to(minecraft, this.parent);
        }
    }
}
