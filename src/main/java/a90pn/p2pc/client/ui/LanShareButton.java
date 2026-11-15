package a90pn.p2pc.client.ui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * 顶替原版「对局域网开放」按钮的占位按钮：只用来标记，行为由构造时传入。
 */
public class LanShareButton extends Button {
    public LanShareButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    // 1.21.11 起 AbstractButton.renderWidget 是 final 的，子类只能实现 renderContents：
    // 这里手动画原版按钮底图 + 居中文字，保持和其它原版按钮一致。
    //? if >=1.21.11 {
    /*@Override
    protected void renderContents(net.minecraft.client.gui.GuiGraphics guiGraphics, int mouseX, int mouseY,
            float partialTick) {
        this.renderDefaultSprite(guiGraphics);
        guiGraphics.drawCenteredString(
                net.minecraft.client.Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + this.getWidth() / 2,
                this.getY() + (this.getHeight() - 8) / 2,
                0xFFFFFF);
    }
    *///?}
}
