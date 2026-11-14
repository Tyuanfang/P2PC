package a90pn.p2pc.client.ui;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * 横向 sprite sheet 播放器：把一张「N 帧并排」的 PNG 按时间切帧画到 GUI 上。
 *
 * <p>贴图约定：所有帧水平排列、等宽等高、帧与帧之间没有间隙；第一帧从左上角开始。
 * 贴图路径写在 {@link #BUNDLE} 里，文件需放在
 * {@code assets/p2pc/textures/anim/<file>.png}，例如
 * {@code p2pc:textures/anim/portal_build.png}。</p>
 *
 * <p>贴图缺失时 {@link #available()} 返回 {@code false}，调用方应当退回到原先的程序化绘制，
 * 这样即使资源没打进 jar 也不会出现紫黑格子。</p>
 *
 * <p>注意：Minecraft 的 GUI 贴图默认是最近邻采样，所以把 16 像素一格的美术放大到 40 多像素
 * 仍然是清晰的像素块，不会糊。</p>
 */
public final class AnimSheet {
    /** 传送门逐块搭建：16 帧，每帧 4×5 个 16px 方块。 */
    public static final AnimSheet PORTAL_BUILD = new AnimSheet(
            "p2pc:textures/anim/portal_build.png", 64, 80, 16, 70, false);
    /** 传送门内部紫漩涡：8 帧循环。 */
    public static final AnimSheet PORTAL_SWIRL = new AnimSheet(
            "p2pc:textures/anim/portal_swirl.png", 32, 48, 8, 80, true);
    /** TNT 点燃下坠：10 帧，放完停在最后一帧。 */
    public static final AnimSheet TNT_FUSE = new AnimSheet(
            "p2pc:textures/anim/tnt_fuse.png", 32, 32, 10, 60, false);
    /** TNT 爆炸：14 帧，放完停在最后一帧。 */
    public static final AnimSheet TNT_BLAST = new AnimSheet(
            "p2pc:textures/anim/tnt_blast.png", 128, 128, 14, 55, false);

    /**
     * 右上角状态栏的「加载中」转圈：源文件是 {@code loading.gif}（64 帧 × 16×16，每帧 20ms），
     * 这里用 {@code tools/anim/convert_loading_gif.py} 转成横向 sprite sheet 再播放。
     */
    public static final AnimSheet LOADING = new AnimSheet(
            "p2pc:textures/gui/loading_frames.png", 16, 16, 64, 20, true);

    private final ResourceLocation texture;
    private final int frameWidth;
    private final int frameHeight;
    private final int frames;
    private final int frameMs;
    private final boolean loop;

    /** 第一次查询过资源是否存在之后就缓存结果，避免每帧都问一遍资源管理器。 */
    private Boolean available;

    private long startedAt;
    private boolean started;

    private AnimSheet(String path, int frameWidth, int frameHeight, int frames, int frameMs, boolean loop) {
        this.texture = ResourceLocation.parse(path);
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frames = frames;
        this.frameMs = frameMs;
        this.loop = loop;
    }

    /** 贴图是否真的在资源包里；用来决定走贴图动画还是退回程序化绘制。 */
    public boolean available() {
        if (available == null) {
            available = Minecraft.getInstance()
                    .getResourceManager()
                    .getResource(texture)
                    .isPresent();
        }
        return available;
    }

    /** 从头开始播放（多次调用等价于重置到第一帧）。 */
    public void restart() {
        this.started = true;
        this.startedAt = Util.getMillis();
    }

    /** 是否已经播放完（非循环动画到最后一帧之后为 true；循环动画永远 false）。 */
    public boolean finished() {
        return !loop && started && elapsedFrame() >= frames;
    }

    /** 当前应该显示的帧下标（0..frames-1）。 */
    public int currentFrame() {
        if (!started) {
            return 0;
        }
        int index = elapsedFrame();
        if (loop) {
            return index % frames;
        }
        return Math.min(index, frames - 1);
    }

    /** 已经走过的帧数（不受循环影响）。 */
    private int elapsedFrame() {
        if (!started) {
            return 0;
        }
        long elapsed = Math.max(0L, Util.getMillis() - startedAt);
        long index = elapsed / frameMs;
        return (int) Math.min(index, Integer.MAX_VALUE);
    }

    /** 直接画某一帧（不做时间推进）。 */
    public void drawFrame(GuiGraphics guiGraphics, int x, int y, int width, int height, int frameIndex) {
        if (!available()) {
            return;
        }
        int index = Math.max(0, Math.min(frameIndex, frames - 1));
        // blit(贴图, 目标x, 目标y, 目标宽, 目标高, 源u, 源v, 源宽, 源高, 贴图总宽, 贴图总高)
        guiGraphics.blit(
                texture,
                x, y, width, height,
                (float) (index * frameWidth), 0.0F,
                frameWidth, frameHeight,
                frames * frameWidth, frameHeight);
    }

    /** 按内部时钟画当前帧。 */
    public void draw(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        drawFrame(guiGraphics, x, y, width, height, currentFrame());
    }

    /** 贴图的原始帧尺寸，方便调用方换算放大倍数。 */
    public int frameWidth() {
        return frameWidth;
    }

    /** 贴图的原始帧尺寸，方便调用方换算放大倍数。 */
    public int frameHeight() {
        return frameHeight;
    }
}
