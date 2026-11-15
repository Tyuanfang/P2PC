package a90pn.p2pc.compat;

import net.minecraft.client.gui.GuiGraphics;

/**
 * 跨版本渲染工具：1.21.11 把 {@code GuiGraphics.pose()} 的返回类型从 {@code PoseStack}
 * 换成了 {@code org.joml.Matrix3x2fStack}，入栈/出栈方法也跟着改名。
 */
public final class Render {
    private Render() {}

    /** 压入一层变换。 */
    public static void push(GuiGraphics graphics) {
        //? if <1.21.11 {
        graphics.pose().pushPose();
        //?} else {
        /*graphics.pose().pushMatrix();
        *///?}
    }

    /** 弹出最近一层变换。 */
    public static void pop(GuiGraphics graphics) {
        //? if <1.21.11 {
        graphics.pose().popPose();
        //?} else {
        /*graphics.pose().popMatrix();
        *///?}
    }

    /** 2D 平移（1.21.11 的矩阵栈是 2D 的，没有 z 分量）。 */
    public static void translate(GuiGraphics graphics, float x, float y) {
        //? if <1.21.11 {
        graphics.pose().translate(x, y, 0.0F);
        //?} else {
        /*graphics.pose().translate(x, y);
        *///?}
    }

    /** 2D 缩放（同上：新版本只接受 x/y）。 */
    public static void scale(GuiGraphics graphics, float x, float y) {
        //? if <1.21.11 {
        graphics.pose().scale(x, y, 1.0F);
        //?} else {
        /*graphics.pose().scale(x, y);
        *///?}
    }
}
