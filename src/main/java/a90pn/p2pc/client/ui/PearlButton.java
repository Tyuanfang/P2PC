package a90pn.p2pc.client.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * 标题界面里的「加入世界」按钮：直接用贴图绘制，悬停时切换到高亮版本。
 *
 * <p>贴图是 20x20 的完整按钮（含外框和末影珍珠图标），所以控件尺寸固定为 20x20，
 * 既保持像素清晰，也和原版按钮行的高度对齐。</p>
 */
public class PearlButton extends Button {
    private static final int SIZE = 20;
    private static final ResourceLocation TEXTURE_IDLE =
            ResourceLocation.fromNamespaceAndPath("p2pc", "textures/gui/open_button_normal.png");
    private static final ResourceLocation TEXTURE_HOVER =
            ResourceLocation.fromNamespaceAndPath("p2pc", "textures/gui/open_button_hower.png");

    public PearlButton(int x, int y, Component message, Component tooltip, OnPress onPress) {
        super(x, y, SIZE, SIZE, message, onPress, DEFAULT_NARRATION);
        this.setTooltip(Tooltip.create(tooltip));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation texture = this.isHoveredOrFocused() ? TEXTURE_HOVER : TEXTURE_IDLE;
        guiGraphics.blit(texture, this.getX(), this.getY(), 0.0F, 0.0F, SIZE, SIZE, SIZE, SIZE);
    }
}
