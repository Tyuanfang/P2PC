package a90pn.p2pc.client.ui;

import a90pn.p2pc.compat.Textures;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

/**
 * 标题界面里的「加入世界」按钮：直接用贴图绘制，悬停时切换到高亮版本。
 *
 * <p>贴图是 20x20 的完整按钮（含外框和末影珍珠图标），所以控件尺寸固定为 20x20，
 * 既保持像素清晰，也和原版按钮行的高度对齐。</p>
 */
public class PearlButton extends Button {
    private static final int SIZE = 20;
    private static final Object TEXTURE_IDLE =
            Textures.of("p2pc", "textures/gui/open_button_normal.png");
    private static final Object TEXTURE_HOVER =
            Textures.of("p2pc", "textures/gui/open_button_hower.png");

    public PearlButton(int x, int y, Component message, Component tooltip, OnPress onPress) {
        super(x, y, SIZE, SIZE, message, onPress, DEFAULT_NARRATION);
        this.setTooltip(Tooltip.create(tooltip));
    }

    //? if <1.21.11 {
    @Override
    protected void renderWidget(
            net.minecraft.client.gui.GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick) {
        this.drawIcon(guiGraphics);
    }
    //?} else {
    /*//? if <26.3 {
    @Override
    protected void renderContents(
            net.minecraft.client.gui.GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick) {
        this.drawIcon(guiGraphics);
    }
    //?} else {
    /^@Override
    protected void extractContents(
            net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick) {
        this.drawIcon(guiGraphics);
    }
    ^///?}
    *///?}

    private void drawIcon(Object graphics) {
        Object texture = this.isHoveredOrFocused() ? TEXTURE_HOVER : TEXTURE_IDLE;
        Textures.blit(
                graphics,
                texture,
                this.getX(),
                this.getY(),
                SIZE,
                SIZE,
                0.0F,
                0.0F,
                SIZE,
                SIZE,
                SIZE,
                SIZE);
    }
}
