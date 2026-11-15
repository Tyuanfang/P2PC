package a90pn.p2pc.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

/**
 * 跨版本贴图工具：1.21.11 把 {@code ResourceLocation} 整体改名为 {@code Identifier}
 * （工厂方法同名，只是类型名变了）。
 *
 * <p>用法：调用方用 {@link #parse(String)} 拿到一个 {@code Object} 形式的贴图 id 存着，
 * 需要判断存在性时调 {@link #exists(Object)}，需要绘制时调
 * {@link #blit(GuiGraphics, Object, int, int, int, int, float, float, int, int, int, int)}。
 * 这样 {@code ResourceLocation} / {@code Identifier} 这两个名字只会出现在本文件里。</p>
 */
public final class Textures {
    private Textures() {}

    /** 解析 {@code namespace:path} 形式的贴图 id。 */
    public static Object parse(String path) {
        //? if <1.21.11 {
        return net.minecraft.resources.ResourceLocation.parse(path);
        //?} else {
        /*return net.minecraft.resources.Identifier.parse(path);
        *///?}
    }

    /** 用命名空间 + 路径拼一个贴图 id。 */
    public static Object of(String namespace, String path) {
        //? if <1.21.11 {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(namespace, path);
        //?} else {
        /*return net.minecraft.resources.Identifier.fromNamespaceAndPath(namespace, path);
        *///?}
    }

    /** 贴图是否真的存在于资源包中。 */
    public static boolean exists(Object texture) {
        //? if <1.21.11 {
        return Minecraft.getInstance()
                .getResourceManager()
                .getResource((net.minecraft.resources.ResourceLocation) texture)
                .isPresent();
        //?} else {
        /*return Minecraft.getInstance()
                .getResourceManager()
                .getResource((net.minecraft.resources.Identifier) texture)
                .isPresent();
        *///?}
    }

    /** 指定源区域与总尺寸地绘制贴图（等价于 GuiGraphics 的 11 参 blit）。 */
    public static void blit(
            GuiGraphics graphics,
            Object texture,
            int x,
            int y,
            int width,
            int height,
            float u,
            float v,
            int uWidth,
            int vHeight,
            int textureWidth,
            int textureHeight) {
        //? if <1.21.11 {
        graphics.blit(
                (net.minecraft.resources.ResourceLocation) texture,
                x, y, width, height, u, v, uWidth, vHeight, textureWidth, textureHeight);
        //?} else {
        /*graphics.blit(
                net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                (net.minecraft.resources.Identifier) texture,
                x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight);
        *///?}
    }
}
