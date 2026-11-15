package a90pn.p2pc.compat;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

/**
 * 取一个界面上已经加进去的控件列表。
 *
 * <p>Fabric 的 {@code Screens.getButtons(Screen)} 在新版本（26.3 起）不再可用，Forge/NeoForge
 * 上也没有这个 API；所以除「Fabric 且旧版本」外，统一从原版 {@code Screen#children()} 里挑出
 * {@link AbstractWidget}。</p>
 */
public final class Widgets {
    private Widgets() {}

    /** 返回界面上的所有控件（保持添加顺序）。 */
    public static List<AbstractWidget> buttons(Screen screen) {
        //? if fabric {
        //? if <26.3 {
        return net.fabricmc.fabric.api.client.screen.v1.Screens.getButtons(screen);
        //?} else {
        /*return collect(screen);
        *///?}
        //?} else {
        /*return collect(screen);
        *///?}
    }

    /** 从原版 children() 里收集控件。 */
    private static List<AbstractWidget> collect(Screen screen) {
        List<AbstractWidget> result = new ArrayList<>();
        for (Object child : screen.children()) {
            if (child instanceof AbstractWidget widget) {
                result.add(widget);
            }
        }
        return result;
    }
}
