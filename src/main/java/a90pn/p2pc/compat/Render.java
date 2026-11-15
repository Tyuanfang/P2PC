package a90pn.p2pc.compat;

/**
 * 跨版本变换工具。
 *
 * <ul>
 *   <li>1.21.11 把 {@code GuiGraphics.pose()} 的返回类型从 {@code PoseStack} 换成了
 *       {@code org.joml.Matrix3x2fStack}，入栈/出栈方法改名成 pushMatrix/popMatrix；</li>
 *   <li>26.3 把 {@code GuiGraphics} 换成了 {@code GuiGraphicsExtractor}。</li>
 * </ul>
 *
 * <p>所以这里统一用 {@code Object} 接收绘制对象，按版本强转。</p>
 */
public final class Render {
    private Render() {}

    /** 压入一层变换。 */
    public static void push(Object graphics) {
        //? if <1.21.11 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).pose().pushPose();
        //?} else {
        /*//? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).pose().pushMatrix();
        //?} else {
        /^((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).pose().pushMatrix();
        ^///?}
        *///?}
    }

    /** 弹出最近一层变换。 */
    public static void pop(Object graphics) {
        //? if <1.21.11 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).pose().popPose();
        //?} else {
        /*//? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).pose().popMatrix();
        //?} else {
        /^((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).pose().popMatrix();
        ^///?}
        *///?}
    }

    /** 2D 平移（1.21.11 的矩阵栈是 2D 的，没有 z 分量）。 */
    public static void translate(Object graphics, float x, float y) {
        //? if <1.21.11 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).pose().translate(x, y, 0.0F);
        //?} else {
        /*//? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).pose().translate(x, y);
        //?} else {
        /^((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).pose().translate(x, y);
        ^///?}
        *///?}
    }

    /** 2D 缩放（同上：新版本只接受 x/y）。 */
    public static void scale(Object graphics, float x, float y) {
        //? if <1.21.11 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).pose().scale(x, y, 1.0F);
        //?} else {
        /*//? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).pose().scale(x, y);
        //?} else {
        /^((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).pose().scale(x, y);
        ^///?}
        *///?}
    }
}
