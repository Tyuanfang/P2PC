package a90pn.p2pc.client.ui;

import a90pn.p2pc.P2PC;
import a90pn.p2pc.Sculk.MassageKind;
import a90pn.p2pc.Sculk.SculkClient;
import a90pn.p2pc.ffi.ConnectionSnapshot;
import a90pn.p2pc.ffi.TunnelStatus;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * 房间运行页：显示邀请码、在线人数，停止后恢复原来的正版验证设置。
 */
public class HostScreen extends Screen {
    private static final int WIDGET_WIDTH = 220;

    private static volatile HostScreen active;

    private final Screen parent;
    private final int port;
    private final boolean previousOnlineMode;

    private EditBox inviteBox;
    private Component statusText = Component.literal("正在创建隧道…");
    private int statusColor = 0xA0A0A0;
    private int players;
    private boolean stopped;
    /** 内容区顶部（竖排布局的基准线）。 */
    private int top;

    public HostScreen(Screen parent, int port, boolean previousOnlineMode) {
        super(Component.literal("房间"));
        this.parent = parent;
        this.port = port;
        this.previousOnlineMode = previousOnlineMode;
    }

    public static HostScreen reopen(Screen parent) {
        Minecraft minecraft = Minecraft.getInstance();
        IntegratedServer server = minecraft.getSingleplayerServer();
        int port = server != null ? server.getPort() : 0;
        return new HostScreen(parent, port, true);
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
        String detail = reason == null || reason.isEmpty() ? message : message + "（" + reason + "）";
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

        this.inviteBox = new EditBox(this.font, x, this.top, WIDGET_WIDTH, 20, Component.literal("邀请码"));
        this.inviteBox.setMaxLength(256);
        this.inviteBox.setEditable(false);
        this.inviteBox.setHint(Component.literal("正在建立隧道…").withStyle(ChatFormatting.GRAY));
        this.addRenderableWidget(this.inviteBox);

        this.addRenderableWidget(Button.builder(Component.literal("复制邀请码"), button -> this.copyInvite())
                .bounds(x, this.top + 24, WIDGET_WIDTH, 20)
                .build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds(x, this.top + 82, WIDGET_WIDTH, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("停止房间"), button -> this.stopRoom())
                .bounds(x, this.top + 106, WIDGET_WIDTH, 20)
                .build());

        TunnelStatus status = P2PC.getInstance().getSculkManager().getTunnelStatus();
        if (status != null) {
            String uri = SculkClient.shareUri(status);
            if (uri != null) {
                this.inviteBox.setValue(uri);
            }
        }
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
        P2PC.getInstance().getSculkManager().close();

        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            IntegratedServer server = minecraft.getSingleplayerServer();
            if (server != null) {
                // 恢复开放前的正版验证设置。
                server.setUsesAuthentication(this.previousOnlineMode);
            }
            minecraft.setScreen(this.parent);
        }
    }

    @Override
    public void tick() {
        super.tick();
        TunnelStatus status = P2PC.getInstance().getSculkManager().getTunnelStatus();
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
        for (ConnectionSnapshot snapshot : status.connections()) {
            if (snapshot.alive()) {
                alive++;
            }
        }
        this.players = alive;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int titleY = Math.max(8, this.top - 30);
        guiGraphics.drawCenteredString(this.font, Component.literal("房间已开启"), this.width / 2, titleY, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font,
                Component.literal("端口 " + this.port), this.width / 2, this.top - 14, 0x9C9C9C);
        guiGraphics.drawCenteredString(this.font, this.statusText,
                this.width / 2, this.top + 48, this.statusColor);
        guiGraphics.drawCenteredString(this.font,
                Component.literal("当前在线：" + this.players),
                this.width / 2, this.top + 60, 0x9C9C9C);
        guiGraphics.drawCenteredString(this.font,
                Component.literal("复制邀请码发给朋友，对方在标题界面点末影珍珠按钮加入"),
                this.width / 2, this.top + 132, 0x707070);
    }

    @Override
    public void onClose() {
        active = null;
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            minecraft.setScreen(this.parent);
        }
    }
}
