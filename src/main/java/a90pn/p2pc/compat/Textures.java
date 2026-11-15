package a90pn.p2pc.compat;

import net.minecraft.client.Minecraft;

/**
 * 跨版本贴图工具：1.21.11 把 {@code ResourceLocation} 整体改名为 {@code Identifier}
 * （工厂方法同名，只是类型名变了）；26.3 又把绘制入口换成 {@code GuiGraphicsExtractor}。
 *
 * <p>用法：调用方用 {@link #parse(String)} / {@link #of(String, String)} 拿到一个
 * {@code Object} 形式的贴图 id 存着，需要判断存在性时调 {@link #exists(Object)}，
 * 需要绘制时调 {@code blit(Object graphics, ...)}。这样版本相关的类型名只出现在本文件里。</p>
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

    /**
     * 指定源区域与总尺寸地绘制贴图。
     *
     * <p>{@code graphics} 可以是 1.21.x 的 {@code GuiGraphics}，也可以是 26.3 的
     * {@code GuiGraphicsExtractor}——这里统一按 {@code Object} 接收再按版本强转。</p>
     */
    public static void blit(
            Object graphics,
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
        ((net.minecraft.client.gui.GuiGraphics) graphics)
                .blit(
                        (net.minecraft.resources.ResourceLocation) texture,
                        x, y, width, height, u, v, uWidth, vHeight, textureWidth, textureHeight);
        //?} else {
        /*//? if <26.3 {
        ((net.minecraft.client.gui.GuiGraphics) graphics)
                .blit(
                        net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                        (net.minecraft.resources.Identifier) texture,
                        x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight);
        //?} else {
        /^((net.minecraft.client.gui.GuiGraphicsExtractor) graphics)
                .blit(
                        net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                        (net.minecraft.resources.Identifier) texture,
                        x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight);
        ^///?}
        *///?}
    }
}
