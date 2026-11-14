package a90pn.p2pc.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;


public class JoinErrorScreen extends Screen {

    private static final long DROP_MS = 750L;

    private static final long BLAST_MS = 420L;
    private static final long SHAKE_MS = 420L;

    private static final ItemStack TNT = new ItemStack(Items.TNT);

    private final Screen parent;
    private final String errorMessage;
    private final long openedAt = Util.getMillis();

    private boolean boomed;
    private boolean playedSound;
    /** 贴图动画是否已经开始（sprite sheet 模式下只 restart 一次）。 */
    private boolean fuseSpriteStarted;
    private boolean blastSpriteStarted;

    public JoinErrorScreen(Screen parent, String errorMessage) {
        super(Component.empty());
        this.parent = parent;
        this.errorMessage = errorMessage == null || errorMessage.isBlank() ? "未知错误" : errorMessage;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("返回主界面"), button -> this.onClose())
                .bounds(this.width / 2 - 100, this.height / 2 + 66, 200, 20)
                .build());
    }

    @Override
    public void tick() {
        super.tick();
        long t = Util.getMillis() - this.openedAt;
        if (!this.boomed && t >= DROP_MS) {
            this.boomed = true;
            if (!this.playedSound) {
                this.playedSound = true;
                Minecraft minecraft = this.minecraft;
                if (minecraft != null) {
                    minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.GENERIC_EXPLODE, 1.0F));
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        long t = Util.getMillis() - this.openedAt;
        float cx = this.width / 2.0F;
        float cy = this.height / 2.0F - 10.0F;

        guiGraphics.pose().pushPose();
        if (this.boomed && t < DROP_MS + SHAKE_MS) {
            float k = 1.0F - ((t - DROP_MS) / (float) SHAKE_MS);
            float shake = 6.0F * k * k;
            guiGraphics.pose().translate(
                    (float) Math.sin(t * 0.11F) * shake,
                    (float) Math.cos(t * 0.17F) * shake,
                    0.0F);
        }
        if (!this.boomed) {
            if (AnimSheet.TNT_FUSE.available()) {
                this.drawFuseSprite(guiGraphics, t, cx, cy);
            } else {
                this.drawTntFalling(guiGraphics, t, cx, cy);
            }
        } else {
            long since = t - DROP_MS;
            if (AnimSheet.TNT_BLAST.available()) {
                // 贴图爆炸，另外叠一层全屏白闪增加冲击感
                if (since < 120L) {
                    int alpha = (int) (220 * (1.0F - since / 120.0F));
                    guiGraphics.fill(0, 0, this.width, this.height, (alpha << 24) | 0xFFFFFF);
                }
                this.drawBlastSprite(guiGraphics, cx, cy);
            } else {
                this.drawExplosion(guiGraphics, since, cx, cy);
            }
        }
        guiGraphics.pose().popPose();

        long textAt = DROP_MS + (long) (BLAST_MS * 0.5);
        if (t >= textAt) {
            guiGraphics.drawCenteredString(this.font, Component.literal("连接失败"), this.width / 2, 20, 0xFF5555);
            List<FormattedCharSequence> lines = this.font.split(Component.literal(this.errorMessage), 320);
            int y = (int) cy + 34;
            int maxLines = Math.min(lines.size(), 6);
            for (int i = 0; i < maxLines; i++) {
                guiGraphics.drawCenteredString(this.font, lines.get(i), this.width / 2, y, 0xFFFFFF);
                y += 12;
            }
            if (lines.size() > maxLines) {
                guiGraphics.drawCenteredString(this.font, Component.literal("…"), this.width / 2, y, 0xA0A0A0);
                y += 12;
            }
            guiGraphics.drawCenteredString(this.font, Component.literal("检查邀请码是否过期，或让房主重新分享"),
                    this.width / 2, y + 6, 0xA0A0A0);
        }
    }

    /** 贴图版 TNT：{@link AnimSheet#TNT_FUSE} 每帧 32×32，TNT 居中占 16×16。 */
    private void drawFuseSprite(GuiGraphics guiGraphics, long t, float cx, float cy) {
        if (!this.fuseSpriteStarted) {
            this.fuseSpriteStarted = true;
            AnimSheet.TNT_FUSE.restart();
        }
        float progress = Math.min(1.0F, t / (float) DROP_MS);
        float eased = progress * progress; // 加速下落
        float topY = -40.0F;
        float landY = cy - 24.0F;
        float y = topY + (landY - topY) * eased;
        // 下坠时轻微上下拉伸，和原版物品掉落的观感接近
        float stretch = 1.0F + 0.12F * Math.min(1.0F, progress * 1.4F);
        int height = Math.round(32 * 3.2F * stretch);
        int width = Math.round(32 * 3.2F / stretch);
        int centerY = Math.round(y + 24.0F);
        AnimSheet.TNT_FUSE.draw(guiGraphics,
                Math.round(cx) - width / 2, centerY - height / 2, width, height);
    }

    /** 贴图版爆炸：{@link AnimSheet#TNT_BLAST} 每帧 128×128，放大到约 200 像素。 */
    private void drawBlastSprite(GuiGraphics guiGraphics, float cx, float cy) {
        if (!this.blastSpriteStarted) {
            this.blastSpriteStarted = true;
            AnimSheet.TNT_BLAST.restart();
        }
        int size = Math.round(128 * 1.6F);
        AnimSheet.TNT_BLAST.draw(guiGraphics,
                Math.round(cx) - size / 2, Math.round(cy) - size / 2, size, size);
    }

    private void drawTntFalling(GuiGraphics guiGraphics, long t, float cx, float cy) {
        float progress = Math.min(1.0F, t / (float) DROP_MS);
        float eased = progress * progress; // 加速下落
        float topY = -40.0F;
        float landY = cy - 24.0F;
        float y = topY + (landY - topY) * eased;
        float scale = 3.2F;
        float stretch = 1.0F + 0.12F * Math.min(1.0F, progress * 1.4F);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(cx, y + 24.0F, 0.0F);
        guiGraphics.pose().scale(scale / stretch, scale * stretch, 1.0F);
        guiGraphics.pose().translate(-8.0F, -8.0F, 0.0F);
        guiGraphics.renderItem(TNT, 0, 0);
        guiGraphics.pose().popPose();

        long sparkSeed = t / 60L;
        for (int i = 0; i < 6; i++) {
            float angle = (i * 1.7F) + sparkSeed * 0.6F;
            float radius = 6.0F + (i % 3) * 5.0F;
            int px = (int) (cx + Mth.cos(angle) * radius);
            int py = (int) (y + 2 + Mth.sin(angle) * radius * 0.4F);
            guiGraphics.fill(px, py, px + 1, py + 1, 0xFFFFC861);
        }
    }

    private void drawExplosion(GuiGraphics guiGraphics, long since, float cx, float cy) {
        float k = Math.min(1.0F, since / (float) BLAST_MS);

        // 白闪
        if (since < 120L) {
            int alpha = (int) (220 * (1.0F - since / 120.0F));
            guiGraphics.fill(0, 0, this.width, this.height, (alpha << 24) | 0xFFFFFF);
        }

        // 冲击环
        for (int ring = 0; ring < 3; ring++) {
            float ringK = Math.min(1.0F, (since - ring * 40.0F) / (BLAST_MS * 1.1F));
            if (ringK <= 0.0F) {
                continue;
            }
            float radius = 10.0F + ringK * 120.0F;
            int alpha = (int) (150 * (1.0F - ringK));
            int color = (alpha << 24) | 0xE9D6FF;
            int points = 72;
            for (int i = 0; i < points; i++) {
                double angle = i * Math.PI * 2 / points;
                int px = (int) (cx + Math.cos(angle) * radius);
                int py = (int) (cy + Math.sin(angle) * radius);
                guiGraphics.fill(px, py, px + 2, py + 2, color);
            }
        }

        // 火球
        int[] fireColors = {0xFF8E2419, 0xFFE8481F, 0xFF8A3D, 0xFFFFC861, 0xFFFFF3C4};
        for (int layer = 0; layer < fireColors.length; layer++) {
            float layerK = Math.min(1.0F, since / (BLAST_MS * (0.5F + layer * 0.28F)));
            if (layerK <= 0.0F) {
                continue;
            }
            float radius = (86.0F - layer * 13.0F) * (0.35F + 0.65F * layerK);
            int alpha = (int) (235 * (1.0F - layerK * 0.75F));
            int color = (alpha << 24) | (fireColors[layer] & 0xFFFFFF);
            drawEllipse(guiGraphics, cx, cy, radius, radius * 0.86F, color);
        }

        // 火花
        for (int i = 0; i < 42; i++) {
            float angle = (i * 2.39996F) + (i % 5) * 0.4F;
            float speed = 60.0F + (i % 7) * 22.0F;
            float life = Math.min(1.0F, since / (BLAST_MS * 1.6F));
            float dist = speed * life;
            float px = cx + Mth.cos(angle) * dist;
            float py = cy + Mth.sin(angle) * dist * 0.8F + 70.0F * life * life;
            int alpha = (int) (255 * (1.0F - life));
            int pxI = (int) px;
            int pyI = (int) py;
            guiGraphics.fill(pxI, pyI, pxI + 2, pyI + 2, (alpha << 24) | ((i % 2 == 0) ? 0xFFC861 : 0xFFFFFF));
        }

        // 烟柱
        for (int i = 0; i < 16; i++) {
            float offset = ((i * 29) % 100) / 100.0F;
            float life = Math.min(1.0F, (since / (BLAST_MS * 2.2F)) + offset * 0.4F);
            float px = cx + (offset - 0.5F) * 90.0F * life;
            float py = cy - 10.0F - life * 60.0F - offset * 20.0F;
            float size = 8.0F + offset * 10.0F + life * 12.0F;
            int alpha = (int) (90 * (1.0F - life));
            int color = (alpha << 24) | 0x3A3A3A;
            drawEllipse(guiGraphics, px, py, size, size * 0.85F, color);
        }

        // 橙色余烬
        float ember = 0.5F + 0.5F * (float) Math.sin(since * 0.02);
        int emberAlpha = (int) (120 * ember * (1.0F - k * 0.6F));
        guiGraphics.fill((int) cx - 3, (int) cy - 3, (int) cx + 3, (int) cy + 3, (emberAlpha << 24) | 0xFFC861);
    }

    private void drawEllipse(GuiGraphics guiGraphics, float cx, float cy, float radiusX, float radiusY, int color) {
        int rows = (int) Math.max(1.0F, radiusY * 2.0F);
        for (int row = 0; row < rows; row++) {
            float dy = -radiusY + row;
            float ratio = dy / radiusY;
            float half = radiusX * (float) Math.sqrt(Math.max(0.0F, 1.0F - ratio * ratio));
            int y = (int) (cy + dy);
            guiGraphics.fill((int) (cx - half), y, (int) (cx + half), y + 1, color);
        }
    }

    @Override
    public void onClose() {
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            minecraft.setScreen(this.parent);
        }
    }
}
