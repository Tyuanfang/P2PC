package a90pn.p2pc.compat;

import java.util.function.Function;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;

/**
 * 跨版本的 {@link CycleButton} 构造工具。
 *
 * <p>1.21.11 起 {@code CycleButton.builder} 的初始值改成了构造时必传的第二个参数，
 * 不再是 1.21.1 那样链式 {@code .withInitialValue(...)}。这里把差异收在一个方法里。</p>
 */
public final class Cycles {
    private Cycles() {}

    /** 造一个「在给定值之间循环」的按钮。 */
    @SafeVarargs
    public static <T> CycleButton<T> cycle(
            Function<T, Component> display,
            T initial,
            int x,
            int y,
            int width,
            int height,
            Component label,
            CycleButton.OnValueChange<T> onChange,
            T... values) {
        //? if <1.21.11 {
        return CycleButton.builder(display)
                .withValues(values)
                .withInitialValue(initial)
                .create(x, y, width, height, label, onChange);
        //?} else {
        /*return CycleButton.builder(display, initial)
                .withValues(values)
                .create(x, y, width, height, label, onChange);
        *///?}
    }
}
