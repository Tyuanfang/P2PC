package a90pn.p2pc.compat;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

/**
 * 跨版本绘制工具。
 *
 * <p>26.3 起 Minecraft 把整个 GUI 绘制入口从 {@code GuiGraphics} 换成了
 * {@code GuiGraphicsExtractor}，并且把文字绘制方法从 {@code drawString/drawCenteredString}
 * 改名成了 {@code text/centeredText}。为了让同一份界面代码在 1.21.1 / 1.21.11 / 26.3
 * 都能编译，这里统一用 {@code Object} 接收绘制对象，内部按版本分支。</p>
 */
public final class Gfx {
    private Gfx() {}

    /** 实心矩形。 */
    public static void fill(Object graphics, int x1, int y1, int x2, int y2, int color) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).fill(x1, y1, x2, y2, color);
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).fill(x1, y1, x2, y2, color);
        *///?}
    }

    /** 竖直渐变矩形。 */
    public static void fillGradient(
            Object graphics, int x1, int y1, int x2, int y2, int topColor, int bottomColor) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics)
                .fillGradient(x1, y1, x2, y2, topColor, bottomColor);
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics)
                .fillGradient(x1, y1, x2, y2, topColor, bottomColor);
        *///?}
    }

    /** 水平 1px 线（两端坐标顺序不限）。 */
    public static void hLine(Object graphics, int x1, int x2, int y, int color) {
        fill(graphics, Math.min(x1, x2), y, Math.max(x1, x2), y + 1, color);
    }

    /** 垂直 1px 线（两端坐标顺序不限）。 */
    public static void vLine(Object graphics, int x, int y1, int y2, int color) {
        fill(graphics, x, Math.min(y1, y2), x + 1, Math.max(y1, y2), color);
    }

    /**
     * 把颜色补成不透明的 ARGB。
     *
     * <p>1.21.11 起 {@code GuiGraphics.drawString} 会先算 {@code ARGB.alpha(color)}，
     * alpha 为 0 就直接 return（整段文字丢弃）；而 1.21.1 及更早的版本会把「不带 alpha
     * 的 0xRRGGBB」当成不透明处理。为了同一份代码在两个版本上表现一致，这里统一把
     * 没有 alpha 的颜色补成 {@code 0xFFRRGGBB}。</p>
     */
    private static int opaqueColor(int color) {
        return (color & 0xFF000000) == 0 ? (color | 0xFF000000) : color;
    }

    /** 左对齐文字（默认带阴影）。 */
    public static void text(Object graphics, Font font, String text, int x, int y, int color) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).drawString(font, text, x, y, opaqueColor(color));
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).text(font, text, x, y, color);
        *///?}
    }

    /** 左对齐文字（默认带阴影）。 */
    public static void text(Object graphics, Font font, Component text, int x, int y, int color) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).drawString(font, text, x, y, opaqueColor(color));
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).text(font, text, x, y, opaqueColor(color));
        *///?}
    }

    /** 居中文字（以 {@code centerX} 为水平中心）。 */
    public static void centeredText(
            Object graphics, Font font, String text, int centerX, int y, int color) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics)
                .drawCenteredString(font, text, centerX, y, opaqueColor(color));
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics)
                .centeredText(font, text, centerX, y, opaqueColor(color));
        *///?}
    }

    /** 居中文字（以 {@code centerX} 为水平中心）。 */
    public static void centeredText(
            Object graphics, Font font, Component text, int centerX, int y, int color) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics)
                .drawCenteredString(font, text, centerX, y, opaqueColor(color));
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics)
                .centeredText(font, text, centerX, y, opaqueColor(color));
        *///?}
    }

    /** 居中文字（以 {@code centerX} 为水平中心）。 */
    public static void centeredText(
            Object graphics, Font font, FormattedCharSequence text, int centerX, int y, int color) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics)
                .drawCenteredString(font, text, centerX, y, opaqueColor(color));
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics)
                .centeredText(font, text, centerX, y, opaqueColor(color));
        *///?}
    }

    /** 画一个物品图标（用于 TNT / 黑曜石这类原版物品）。 */
    public static void item(
            Object graphics, net.minecraft.world.item.ItemStack stack, int x, int y) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).renderItem(stack, x, y);
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).item(stack, x, y);
        *///?}
    }

    /** 开启裁剪区域。 */
    public static void enableScissor(Object graphics, int x1, int y1, int x2, int y2) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).enableScissor(x1, y1, x2, y2);
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).enableScissor(x1, y1, x2, y2);
        *///?}
    }

    /** 关闭裁剪区域。 */
    public static void disableScissor(Object graphics) {
        //? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics).disableScissor();
        //?} else {
        /*((net.minecraft.client.gui.GuiGraphicsExtractor) graphics).disableScissor();
        *///?}
    }
}
