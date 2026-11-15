package a90pn.p2pc.compat;

/**
 * 跨版本时间工具：{@code net.minecraft.Util} 在 1.21.11 被移动到了 {@code net.minecraft.util.Util}。
 *
 * <p>把版本差异关在这一个方法里，其它文件只调 {@link #millis()}，就不必到处写条件注释。</p>
 */
public final class Time {
    private Time() {}

    /** 单调递增的毫秒时间戳（等价于原版 {@code Util.getMillis()}）。 */
    public static long millis() {
        //? if <1.21.11 {
        return net.minecraft.Util.getMillis();
        //?} else {
        /*return net.minecraft.util.Util.getMillis();
        *///?}
    }
}
