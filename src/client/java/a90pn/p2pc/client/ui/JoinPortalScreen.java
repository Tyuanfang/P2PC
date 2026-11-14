package a90pn.p2pc.client.ui;

import a90pn.p2pc.P2PC;
import a90pn.p2pc.Sculk.MassageKind;
import a90pn.p2pc.Sculk.SculkManager;
import a90pn.p2pc.Sculk.SculkStatues;
import a90pn.p2pc.ffi.JoinConfig;
import a90pn.p2pc.ffi.JoinOptions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class JoinPortalScreen extends Screen {
    public static final int LOCAL_PORT = 25565;

    private static final int COLS = 4;
    private static final int ROWS = 5;
    private static final int[] FRAME = {16, 17, 18, 19, 15, 11, 7, 3, 2, 1, 0, 4, 8, 12};
    private static final long BLOCK_START_MS = 380L;
    private static final long BLOCK_STEP_MS = 150L;
    private static final long IGNITE_MS = 220L;
    private static final long TIMEOUT_MS = 45_000L;
    private static final long ENTER_DELAY_MS = 700L;

    private static final ItemStack OBSIDIAN = new ItemStack(Items.OBSIDIAN);

    private final String inviteCode;
    private final Screen parent;
    private final long openedAt = Util.getMillis();

    private boolean started;
    private boolean connected;
    private boolean failed;
    private boolean entered;
    private boolean igniteSoundPlayed;
    private long connectedAt;
    private String statusText = "正在连接服务器…";

    public JoinPortalScreen(String inviteCode, Screen parent) {
        super(Component.empty());
        this.inviteCode = inviteCode;
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("取消"), button -> this.onClose())
                .bounds(this.width / 2 - 60, this.height - 30, 120, 20)
                .build());
        if (!this.started) {
            this.started = true;
            this.startJoin();
        }
    }

    private void startJoin() {
        SculkManager manager = P2PC.getInstance().getSculkManager();
        SculkStatues state = manager.getStatus();
        // 正在开房间时不能再加入：直接报错，别动房间
        if (state == SculkStatues.HOST) {
            this.abortWithError("你正在开房间，请先停止房间再加入别人的世界");
            return;
        }
        // 上一次的加入/错误状态先复位，否则 join 会直接返回、这里只能干等超时
        if (state == SculkStatues.JOIN || state == SculkStatues.ERROR) {
            try {
                manager.close();
            } catch (Throwable ignored) {
                // 复位失败也继续尝试
            }
        }
        manager.join(
                new JoinOptions(this.inviteCode, (short) LOCAL_PORT, new JoinConfig(100L, 5000L, 1000L)),
                (status, kind, message, reason) -> Minecraft.getInstance()
                        .execute(() -> this.onClientEvent(kind, message, reason)));
    }

    /** 直接跳错误页，不关闭隧道（用于「正在开房间」这类不该动房间的情况）。 */
    private void abortWithError(String message) {
        if (this.failed) {
            return;
        }
        this.failed = true;
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            minecraft.setScreen(new JoinErrorScreen(this.parent, message));
        }
    }

    private void onClientEvent(MassageKind kind, String message, String reason) {
        if (this.failed || this.entered) {
            return;
        }
        if (kind == MassageKind.ERROR) {
            this.fail(reason == null || reason.isBlank() ? message : message + "（" + reason + "）");
            return;
        }
        if (!this.connected && kind == MassageKind.INFO && "已连接".equals(message)) {
            this.connected = true;
            this.connectedAt = Util.getMillis();
            this.statusText = "隧道已建立，正在进入世界…";
            this.playLocalSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F);
            return;
        }
        if (kind == MassageKind.WARNING && "连接断开".equals(message)) {
            this.fail("连接已断开，请让房主重新分享邀请码");
            return;
        }
        if (reason != null && !reason.isBlank()) {
            this.statusText = message + "（" + reason + "）";
        } else if (message != null && !message.isBlank()) {
            this.statusText = message;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.failed || this.entered) {
            return;
        }
        long now = Util.getMillis();
        if (this.connected) {
            if (now - this.connectedAt >= ENTER_DELAY_MS) {
                this.entered = true;
                this.enterWorld();
            }
            return;
        }
        if (now - this.openedAt >= TIMEOUT_MS) {
            this.fail("连接超时：45 秒内没有收到服务器响应");
        }
    }

    private void enterWorld() {
        Minecraft minecraft = this.minecraft;
        if (minecraft == null) {
            return;
        }
        String address = "127.0.0.1:" + LOCAL_PORT;
        // 普通的登录连接：TransferState 必须传 null——传非 null 会被原版当成「转移连接」，
        // 服务器会直接回「此服务器不接受转移」。parent 用标题界面，断开时不要退回这个过场页。
        ConnectScreen.startConnecting(
                this.parent,
                minecraft,
                ServerAddress.parseString(address),
                new ServerData("P2PC 世界", address, ServerData.Type.OTHER),
                false,
                null);
    }

    private void fail(String message) {
        if (this.failed) {
            return;
        }
        this.failed = true;
        try {
            P2PC.getInstance().getSculkManager().close();
        } catch (Throwable ignored) {
            // 关闭失败挡住错误页
        }
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            minecraft.setScreen(new JoinErrorScreen(this.parent, message));
        }
    }

    @Override
    public void onClose() {
        if (!this.entered && !this.failed) {
            try {
                P2PC.getInstance().getSculkManager().close();
            } catch (Throwable ignored) {
                // 忽略
            }
        }
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            minecraft.setScreen(this.parent);
        }
    }

    // ------------------------------------------------------------------
    // 渲染
    // ------------------------------------------------------------------

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        long now = Util.getMillis();
        long elapsed = now - this.openedAt;

        float block = Math.min(46.0F, Math.max(20.0F, (this.height - 190.0F) / ROWS));
        float originX = this.width / 2.0F - COLS * block / 2.0F;
        float originY = this.height / 2.0F - ROWS * block / 2.0F + 4.0F;

        guiGraphics.drawCenteredString(this.font, Component.literal("正在建立连接"), this.width / 2, 20, 0xFFFFFF);

        float lastBlockAt = BLOCK_START_MS + (FRAME.length - 1) * BLOCK_STEP_MS;
        boolean ignited = elapsed >= lastBlockAt + IGNITE_MS * 0.35F;
        this.drawPortal(guiGraphics, elapsed, block, originX, originY, ignited);
        if (ignited && !this.igniteSoundPlayed) {
            this.igniteSoundPlayed = true;
            this.playLocalSound(SoundEvents.PORTAL_TRIGGER, 0.7F);
        }
        if (ignited) {
            this.drawInterior(guiGraphics, elapsed, block, originX, originY);
        }
        this.drawStatus(guiGraphics, elapsed, block, originX, originY);
    }

    private void drawPortal(GuiGraphics guiGraphics, long elapsed, float block, float originX, float originY,
                            boolean ignited) {
        PoseStack pose = guiGraphics.pose();
        float lastBlockAt = BLOCK_START_MS + (FRAME.length - 1) * BLOCK_STEP_MS;

        for (int i = 0; i < FRAME.length; i++) {
            long t = elapsed - (BLOCK_START_MS + i * BLOCK_STEP_MS);
            if (t < 0) {
                continue;
            }
            int cell = FRAME[i];
            float x = originX + (cell % COLS) * block;
            float y = originY + (cell / COLS) * block;

            float appear = Math.min(1.0F, t / 150.0F);
            float drop = (1.0F - easeOutBack(appear)) * block * 0.9F;
            float scale = block / 16.0F * (0.75F + 0.25F * appear);
            float pop = 1.0F + 0.10F * (float) Math.sin(appear * Math.PI);

            pose.pushPose();
            pose.translate(x + block / 2.0F, y + block / 2.0F - drop, 0.0F);
            pose.scale(scale * pop, scale * pop, 1.0F);
            pose.translate(-8.0F, -8.0F, 0.0F);
            guiGraphics.renderItem(OBSIDIAN, 0, 0);
            pose.popPose();

            // 落地灰尘
            if (t < 360L) {
                float dust = 1.0F - t / 360.0F;
                int alpha = (int) (150 * dust * dust);
                int color = (alpha << 24) | 0x8A8A8A;
                for (int d = 0; d < 4; d++) {
                    float dx = x + block / 2.0F + (d - 1.5F) * block * 0.22F;
                    float dy = y + block - 2 + (d % 2) * 2;
                    guiGraphics.fill((int) dx, (int) dy, (int) dx + 2, (int) dy + 2, color);
                }
            }
        }

        // 点燃闪光
        float sinceIgnite = elapsed - lastBlockAt;
        if (sinceIgnite >= 0 && sinceIgnite < IGNITE_MS * 2) {
            float k = sinceIgnite / (IGNITE_MS * 2);
            int alpha = (int) (200 * (1.0F - k));
            int color = (alpha << 24) | 0xF3E7FF;
            float cx = originX + COLS * block / 2.0F;
            float cy = originY + ROWS * block / 2.0F;
            float radius = block * (1.0F + k * 3.0F);
            int left = Math.max(0, (int) (cx - radius));
            int right = Math.min(this.width, (int) (cx + radius));
            int top = Math.max(0, (int) (cy - radius));
            int bottom = Math.min(this.height - 44, (int) (cy + radius));
            if (right > left && bottom > top) {
                guiGraphics.fill(left, top, right, bottom, color);
            }
        }
        if (ignited) {
            // 门框呼吸光
            float pulse = 0.5F + 0.5F * (float) Math.sin(elapsed * 0.004);
            int alpha = (int) (40 + 60 * pulse);
            int color = (alpha << 24) | 0xB06BFF;
            guiGraphics.hLine((int) originX, (int) (originX + COLS * block - 1), (int) (originY - 1), color);
            guiGraphics.hLine((int) originX, (int) (originX + COLS * block - 1),
                    (int) (originY + ROWS * block), color);
            guiGraphics.vLine((int) (originX - 1), (int) originY, (int) (originY + ROWS * block), color);
            guiGraphics.vLine((int) (originX + COLS * block), (int) originY,
                    (int) (originY + ROWS * block), color);
        }
    }

    private void drawInterior(GuiGraphics guiGraphics, long elapsed, float block, float originX, float originY) {
        float ix = originX + block;
        float iy = originY + block;
        float iw = 2 * block;
        float ih = 3 * block;
        float cx = ix + iw / 2.0F;
        float cy = iy + ih / 2.0F;

        float pulse = 0.5F + 0.5F * (float) Math.sin(elapsed * 0.0035);
        int top = withAlpha(0x1E0A33, 0.85F);
        int bottom = withAlpha(0x6F24C9, 0.75F + 0.20F * pulse);
        guiGraphics.fillGradient((int) ix, (int) iy, (int) (ix + iw), (int) (iy + ih), top, bottom);

        // 旋涡手臂
        int arms = 9;
        for (int arm = 0; arm < arms; arm++) {
            float base = elapsed * (this.connected ? 0.0034F : 0.0016F) + arm * (Mth.TWO_PI / arms);
            for (int step = 0; step < 8; step++) {
                float ratio = (step + 1) / 8.0F;
                float radius = ratio * ih * 0.5F;
                float angle = base + ratio * 2.2F;
                float px = cx + Mth.cos(angle) * radius * 0.45F;
                float py = cy + Mth.sin(angle) * radius * 0.85F;
                int alpha = (int) (40 + 150 * ratio);
                int color = (alpha << 24) | 0xE9D6FF;
                int size = ratio > 0.6F ? 3 : 2;
                guiGraphics.fill((int) px, (int) py, (int) px + size, (int) py + size, color);
            }
        }

        // 上升的紫色粒子
        for (int i = 0; i < 18; i++) {
            float offset = ((i * 37) % 100) / 100.0F;
            float speed = 0.0010F + ((i * 13) % 9) * 0.00006F;
            float progress = (elapsed * speed + offset) % 1.0F;
            float px = ix + 3 + ((i * 53) % Math.max(1, (int) iw - 6));
            float py = iy + ih - progress * (ih - 4);
            int alpha = (int) (180 * (1.0F - progress));
            int color = (alpha << 24) | 0xD9A7FF;
            guiGraphics.fill((int) px, (int) py, (int) px + 1, (int) py + 1, color);
        }

        // 连接成功的白闪 + 扩散环
        if (this.connected) {
            long since = Util.getMillis() - this.connectedAt;
            if (since < 500L) {
                float k = since / 500.0F;
                int alpha = (int) (170 * (1.0F - k));
                guiGraphics.fill((int) ix, (int) iy, (int) (ix + iw), (int) (iy + ih),
                        (alpha << 24) | 0xFFFFFF);
                float radius = block * (0.5F + k * 2.6F);
                int ring = (int) (140 * (1.0F - k));
                drawRing(guiGraphics, cx, cy, radius, (ring << 24) | 0xA877FF);
            }
        }
    }

    private void drawStatus(GuiGraphics guiGraphics, long elapsed, float block, float originX, float originY) {
        int textY = (int) (originY + ROWS * block + 14);
        int dots = (int) ((elapsed / 350) % 4);
        String waiting = this.connected ? "连接成功" : "正在建立隧道" + ".".repeat(dots);
        guiGraphics.drawCenteredString(this.font, Component.literal(waiting), this.width / 2, textY, 0xFFFFFF);
        if (!this.connected) {
            long seconds = Math.max(0L, (elapsed) / 1000L);
            guiGraphics.drawCenteredString(this.font, Component.literal("已等待 " + seconds + " 秒"),
                    this.width / 2, textY + 13, 0xA0A0A0);
        }
        if (this.statusText != null && !this.statusText.isEmpty() && this.height - 70 > textY + 26) {
            guiGraphics.drawCenteredString(this.font, Component.literal(this.statusText),
                    this.width / 2, textY + 26, 0x7F7FC0);
        }
    }

    private void drawRing(GuiGraphics guiGraphics, float cx, float cy, float radius, int color) {
        int points = 64;
        for (int i = 0; i < points; i++) {
            double angle = i * Math.PI * 2 / points;
            int px = (int) (cx + Math.cos(angle) * radius);
            int py = (int) (cy + Math.sin(angle) * radius);
            guiGraphics.fill(px, py, px + 2, py + 2, color);
        }
    }

    private static float easeOutBack(float t) {
        float c1 = 1.70158F;
        float c3 = c1 + 1.0F;
        float p = t - 1.0F;
        return 1.0F + c3 * p * p * p + c1 * p * p;
    }

    private static int withAlpha(int rgb, float alpha) {
        int a = Mth.clamp((int) (alpha * 255.0F), 0, 255);
        return (a << 24) | (rgb & 0xFFFFFF);
    }

    private void playLocalSound(net.minecraft.sounds.SoundEvent sound, float pitch) {
        Minecraft minecraft = this.minecraft;
        if (minecraft == null) {
            return;
        }
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch));
    }
}
