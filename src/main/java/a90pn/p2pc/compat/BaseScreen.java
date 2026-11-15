package a90pn.p2pc.compat;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * 跨版本界面基类。
 *
 * <p>26.3 把 {@code Screen.render(GuiGraphics, int, int, float)} 换成了
 * {@code extractRenderState(GuiGraphicsExtractor, int, int, float)}。子类只需要实现
 * {@link #drawContents(Object, int, int, float)}，把「原版背景 + 控件已经画完之后」要补的内容
 * 画上去即可；绘制对象按 {@code Object} 传递，配合 {@link Gfx} / {@link Textures} / {@link Render}
 * 使用。</p>
 */
public abstract class BaseScreen extends Screen {
    protected BaseScreen(Component title) {
        super(title);
    }

    //? if <26.3 {
    @Override
    public void render(
            net.minecraft.client.gui.GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.drawContents(graphics, mouseX, mouseY, partialTick);
    }
    //?} else {
    /*@Override
    public void extractRenderState(
            net.minecraft.client.gui.GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        this.drawContents(graphics, mouseX, mouseY, partialTick);
    }
    *///?}

    /** 在原版背景与控件之后绘制本界面的内容。 */
    protected abstract void drawContents(Object graphics, int mouseX, int mouseY, float partialTick);
}
