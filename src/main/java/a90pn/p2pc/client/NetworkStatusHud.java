package a90pn.p2pc.client;

import a90pn.p2pc.P2PC;
import a90pn.p2pc.Sculk.SculkManager;
import a90pn.p2pc.Sculk.SculkStatues;
import a90pn.p2pc.client.ui.AnimSheet;
import a90pn.p2pc.ffi.ConnectionSnapshot;
import a90pn.p2pc.ffi.TunnelPhase;
import a90pn.p2pc.ffi.TunnelState;
import a90pn.p2pc.ffi.TunnelStatus;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.resources.ResourceLocation;

/**
 * 右上角的联机网络状态图标队列。
 *
 * <p>只在「通过 P2PC 加入别人的世界」（{@link SculkStatues#JOIN}）时显示，房主与自己开的世界不显示。
 * 同一时刻可能有多个状态，所以这里按队列从右往左排列，每个图标配一块半透明底板保证在亮背景上也看得清。</p>
 *
 * <p>当前支持的状态（对应 {@code assets/p2pc/textures/gui/} 下的贴图）：</p>
 * <ul>
 *   <li>{@code loading_frames.png}：隧道还没建立起来（STARTING），转圈动画；</li>
 *   <li>{@code alternate_node.png}：连接从直连切到了中继（走备用节点），切换后短暂显示；</li>
 *   <li>{@code high_ping.png}：当前最佳连接的 RTT 超过阈值。</li>
 * </ul>
 */
public final class NetworkStatusHud implements HudRenderCallback {
    /** 单个图标的逻辑尺寸（像素）。 */
    private static final int ICON = 16;
    /** 图标之间的间距。 */
    private static final int GAP = 2;
    /** 距离屏幕右上角的边距。 */
    private static final int MARGIN = 4;
    /** RTT 超过这个值就算「高延迟」。 */
    private static final long HIGH_PING_MS = 200L;
    /** 切到中继后「备用节点」图标保留多久。 */
    private static final long ALTERNATE_HOLD_MS = 4000L;

    private static final ResourceLocation HIGH_PING_ICON =
            ResourceLocation.parse("p2pc:textures/gui/high_ping.png");
    private static final ResourceLocation ALTERNATE_NODE_ICON =
            ResourceLocation.parse("p2pc:textures/gui/alternate_node.png");

    /** 上一次的最佳路径是否走中继；用来检测「直连 → 中继」的切换。 */
    private boolean lastRelay;
    private boolean hasLastRelay;
    /** 「备用节点」图标要保留到什么时候（毫秒时间戳）。 */
    private long alternateNodeUntil;
    /** 加载动画是否已经 restart 过，避免每帧都重置到第 0 帧。 */
    private boolean loadingStarted;

    /**
     * Fabric 的 HUD 回调。只画图标，不拦截任何输入。
     */
    @Override
    public void onHudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        List<ResourceLocation> queue = collectQueue();
        if (queue.isEmpty()) {
            return;
        }
        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int x = width - MARGIN;
        int y = MARGIN;
        for (int i = queue.size() - 1; i >= 0; i--) {
            ResourceLocation texture = queue.get(i);
            x -= ICON;
            guiGraphics.fill(x - 1, y - 1, x + ICON + 1, y + ICON + 1, 0x66000000);
            if (texture == null) {
                // null 代表「加载中」，走多帧动画
                AnimSheet.LOADING.draw(guiGraphics, x, y, ICON, ICON);
            } else {
                // 源图是 64×64，画到 16×16；GUI 贴图是最近邻采样，缩小后仍然是清晰的像素图标
                guiGraphics.blit(texture, x, y, ICON, ICON, 0.0F, 0.0F, 64, 64, 64, 64);
            }
            x -= GAP;
        }
    }

    /**
     * 根据当前隧道状态收集要显示的状态图标（左→右的顺序即队列顺序）。
     * 没有联机、不在加入状态时返回空列表并清空内部状态。
     */
    private List<ResourceLocation> collectQueue() {
        P2PC mod = P2PC.getInstance();
        if (mod == null) {
            reset();
            return List.of();
        }
        SculkManager manager = mod.getSculkManager();
        if (manager == null || manager.getStatus() != SculkStatues.JOIN) {
            reset();
            return List.of();
        }
        TunnelStatus status = manager.getTunnelStatus();
        if (status == null) {
            return List.of();
        }

        List<ResourceLocation> queue = new ArrayList<>(3);
        boolean loading = true;
        boolean relay = false;
        boolean highPing = false;

        TunnelState state = status.state();
        TunnelPhase phase = state == null ? null : state.phase();
        if (phase == TunnelPhase.ACTIVE || phase == TunnelPhase.STOPPING) {
            loading = false;
            ConnectionSnapshot best = null;
            for (ConnectionSnapshot snapshot : status.connections()) {
                if (snapshot == null || !snapshot.alive()) {
                    continue;
                }
                if (best == null || snapshot.rttMs() < best.rttMs()) {
                    best = snapshot;
                }
            }
            if (best != null) {
                relay = best.isRelay();
                highPing = best.rttMs() >= HIGH_PING_MS;
            }
            // 直连 → 中继：说明直连断了、切到备用节点，图标保留几秒提醒玩家
            if (this.hasLastRelay && !this.lastRelay && relay) {
                this.alternateNodeUntil = Util.getMillis() + ALTERNATE_HOLD_MS;
            }
            this.lastRelay = relay;
            this.hasLastRelay = true;
        }

        if (loading) {
            if (!this.loadingStarted) {
                this.loadingStarted = true;
                AnimSheet.LOADING.restart();
            }
            // null 是「加载中」的占位，见 onHudRender
            queue.add(null);
        }
        if (Util.getMillis() < this.alternateNodeUntil) {
            queue.add(ALTERNATE_NODE_ICON);
        }
        if (highPing) {
            queue.add(HIGH_PING_ICON);
        }
        return queue;
    }

    /** 退出联机状态时清空残留标记，下次联机重新开始判断。 */
    private void reset() {
        this.lastRelay = false;
        this.hasLastRelay = false;
        this.alternateNodeUntil = 0L;
        this.loadingStarted = false;
    }
}
