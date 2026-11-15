package a90pn.p2pc.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/**
 * 切换当前 {@link Screen}。
 *
 * <p>26.3 起 {@code Minecraft#setScreen(Screen)} 改名为 {@code setScreenAndShow(Screen)}，
 * 其它版本还是旧名字。所有界面跳转都走这里，避免每个文件各写一遍条件分支。</p>
 */
public final class ScreenSwap {
    private ScreenSwap() {}

    /** 把 {@code client} 当前显示的界面换成 {@code screen}。 */
    public static void to(Minecraft client, Screen screen) {
        //? if <26.3 {
        client.setScreen(screen);
        //?} else {
        /*client.setScreenAndShow(screen);
        *///?}
    }
}
