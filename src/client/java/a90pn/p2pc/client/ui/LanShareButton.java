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
}
