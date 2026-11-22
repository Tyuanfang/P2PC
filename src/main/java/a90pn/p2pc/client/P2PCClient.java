package a90pn.p2pc.client;

import a90pn.p2pc.P2PC;
import a90pn.p2pc.Sculk.SculkStatues;
import a90pn.p2pc.client.ui.HostScreen;
import a90pn.p2pc.client.ui.HostUI;
import a90pn.p2pc.client.ui.JoinUI;
import a90pn.p2pc.client.ui.LanShareButton;
import a90pn.p2pc.client.ui.PearlButton;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

//? if fabric {
public class P2PCClient implements net.fabricmc.api.ClientModInitializer {
	@Override
	public void onInitializeClient() {
		P2PCClient.init();
	}
//?} else {
/*public final class P2PCClient {
	private P2PCClient() {}
*///?}
	/**
	 * 注册客户端钩子。Fabric 走 Fabric API 的事件，Forge/NeoForge 走各自的 EVENT_BUS。
	 * 三个加载器最终都落到同一组回调方法上，业务逻辑不重复。
	 */
	public static void init() {
		//? if fabric {
		net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT
				.register((handler, client) -> onDisconnect());
		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK
				.register(client -> LanAnnouncer.tick());
		// 26.3 起旧的 HudRenderCallback 被 HudElementRegistry 取代。
		//? if <26.3 {
		net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT
				.register(NetworkStatusHud.INSTANCE);
		//?} else {
		/*net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.addLast(
				net.minecraft.resources.Identifier.fromNamespaceAndPath("p2pc", "network_status_icons"),
				NetworkStatusHud.INSTANCE);
		*///?}
		net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AFTER_INIT
				.register((client, screen, scaledWidth, scaledHeight) -> onScreenInit(screen));
		//?} else {
		/*//? if neoforge {
		/^//? if <26.3 {
		// NeoForge 21.x 还是老总线：按事件类型 addListener(Consumer)。
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
				(net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) -> onDisconnect());
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
				(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) -> LanAnnouncer.tick());
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
				(net.neoforged.neoforge.client.event.RenderGuiEvent.Post event) -> NetworkStatusHud.INSTANCE
						.render(event.getGuiGraphics()));
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
				(net.neoforged.neoforge.client.event.ScreenEvent.Init.Post event) -> onScreenInit(event.getScreen()));
		//?} else {
		/^// NeoForge 26.x 的事件总线换了注册方式：addListener(Class, Consumer)。
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
				net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut.class,
				event -> onDisconnect());
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
				net.neoforged.neoforge.client.event.ClientTickEvent.Post.class,
				event -> LanAnnouncer.tick());
		// RenderGuiEvent.Post 在游戏总线上，不需要拿到 mod 总线的 IEventBus
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
				net.neoforged.neoforge.client.event.RenderGuiEvent.Post.class,
				event -> NetworkStatusHud.INSTANCE.render(event.getGuiGraphics()));
		net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
				net.neoforged.neoforge.client.event.ScreenEvent.Init.Post.class,
				event -> onScreenInit(event.getScreen()));
		^///?}
		^///?} else {
		//? if <1.21.11 {
		// Forge 1.21.1：老总线，事件按类型直接 addListener(Consumer)。HUD 覆盖层在 mod 总线上，见 initForgeModBus。
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
				(net.minecraftforge.event.TickEvent.ClientTickEvent.Post event) -> LanAnnouncer.tick());
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
				(net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) -> onDisconnect());
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
				(net.minecraftforge.client.event.ScreenEvent.Init.Post event) -> onScreenInit(event.getScreen()));
		//?} else {
		/^// Forge 1.21.11 起总线是 7.x：每个事件类自己带静态 BUS 字段，直接 addListener(Consumer)。
		net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut.BUS
				.addListener(event -> onDisconnect());
		net.minecraftforge.event.TickEvent.ClientTickEvent.Post.BUS
				.addListener(event -> LanAnnouncer.tick());
		net.minecraftforge.client.event.ScreenEvent.Init.Post.BUS
				.addListener(event -> onScreenInit(event.getScreen()));
		// HUD：Forge 走「分层 GUI 覆盖层」，由 AddGuiOverlayLayersEvent 注册一层
		net.minecraftforge.client.event.AddGuiOverlayLayersEvent.BUS.addListener(event -> event.getLayeredDraw()
				.add(net.minecraft.resources.Identifier.fromNamespaceAndPath("p2pc", "network_status_icons"),
						(graphics, deltaTracker) -> NetworkStatusHud.INSTANCE.render(graphics)));
		^///?}
		//?}
		*///?}
	}

	//? if forge {
	/*//? if <1.21.11 {
	/^*
	 * Forge 1.21.1 专用：GUI 覆盖层的注册事件在 mod 总线上，只能通过 {@code @Mod} 构造器注入的
	 * {@code IEventBus} 拿到。之后版本换成了每个事件类自带的静态 BUS，就不需要这个入口了。
	 ^/
	public static void initForgeModBus(net.minecraftforge.eventbus.api.IEventBus modBus) {
		//? if >=1.21.1 {
		modBus.addListener((net.minecraftforge.client.event.AddGuiOverlayLayersEvent event) -> event.getLayeredDraw()
				.add(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("p2pc", "network_status_icons"),
						(graphics, deltaTracker) -> NetworkStatusHud.INSTANCE.render(graphics)));
		//?}
		// 1.21（51.x）还没有 GUI 覆盖层注册事件：AddGuiOverlayLayersEvent 是从 1.21.1 才加的，
		// 所以 1.21-forge 上这个方法是空实现，右上角不显示网络状态图标（其余功能不受影响）。
	}
	//?}
	*///?}

	/**
	 * 离开服务器/世界时把隧道收掉：否则管理器会一直开着 watch，
	 * 连接状态日志刷个不停（连接被拒、断线、退出世界都会走到这里）。
	 */
	private static void onDisconnect() {
		LanAnnouncer.stop();
		a90pn.p2pc.Sculk.SculkManager manager = P2PC.manager();
		if (manager == null) {
			return;
		}
		// close() 内部会 stop().get(2s)，在客户端线程上直接调会卡住断开流程，放后台线程。
		Thread cleanup = new Thread(() -> {
			try {
				manager.close();
			} catch (RuntimeException ignored) {
				// 断开时收尾失败无碍：下次连接会重建服务对象
			}
		}, "p2pc-disconnect-close");
		cleanup.setDaemon(true);
		cleanup.start();
	}

	/** 每个界面初始化完成后注入我们的入口按钮。 */
	private static void onScreenInit(Screen screen) {
		if (screen instanceof TitleScreen) {
			List<AbstractWidget> buttons = a90pn.p2pc.compat.Widgets.buttons(screen);
			// 窗口缩放/重新 init 时会再次触发，避免叠加出第二个按钮。
			if (buttons.stream().anyMatch(widget -> widget instanceof PearlButton)) {
				return;
			}
			// 贴在按钮列的右侧、和第二个按钮（多人游戏）同一行，和原版布局保持一条线。
			AbstractWidget anchor = buttons.size() > 1 ? buttons.get(1) : null;
			int x = anchor != null ? anchor.getX() + anchor.getWidth() + 8 : screen.width - 40;
			int y = anchor != null ? anchor.getY() : screen.height / 2 - 10;
			buttons.add(new PearlButton(x, y, Component.empty(), Component.literal("加入世界"),
					button -> a90pn.p2pc.compat.ScreenSwap.to(Minecraft.getInstance(), new JoinUI(Component.empty(), screen))));
		} else if (screen instanceof PauseScreen && Minecraft.getInstance().getSingleplayerServer() != null) {
			replaceShareToLanButton(screen);
		}
	}

	/**
	 * 把原版「对局域网开放」按钮换成我们的入口：原位、原文字，点开是带 P2PC 选项的创建房间页。
	 *
	 * <p>注意：世界已经开放到局域网后，原版会把槽位换成「举报玩家」（isPublished 分支），
	 * 所以两种文字都要认；此时按钮文字换成「管理房间」，点开是房间管理页。</p>
	 */
	private static void replaceShareToLanButton(Screen screen) {
		List<AbstractWidget> buttons = a90pn.p2pc.compat.Widgets.buttons(screen);
		if (buttons.stream().anyMatch(widget -> widget instanceof LanShareButton)) {
			return;
		}

		Component shareLabel = Component.translatable("menu.shareToLan");
		String expectedShare = shareLabel.getString();
		String expectedReport = Component.translatable("menu.playerReporting").getString();
		for (AbstractWidget widget : buttons) {
			if (!(widget instanceof Button)) {
				continue;
			}
			String text = widget.getMessage().getString();
			if (text.equals(expectedShare) || text.equals(expectedReport)) {
				// 原版按钮已经用不上了：藏起来，别让点击穿透到它。
				widget.visible = false;
				widget.active = false;
				boolean hosting = isHosting();
				Component label = hosting ? Component.literal("管理房间") : shareLabel;
				buttons.add(new LanShareButton(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(),
						label, button -> openHostPanel(screen)));
				return;
			}
		}

		// 兼容别的「自定义局域网联机」模组：它们可能把原版按钮的文字/位置也改掉了，
		// 这时按文字找不到槽位。如果正在开房间，就在按钮列最下面补一个管理入口，保证还能回去看邀请码。
		boolean hosting = isHosting();
		if (!hosting || buttons.isEmpty()) {
			return;
		}
		AbstractWidget last = buttons.get(buttons.size() - 1);
		int y = last.getY() + last.getHeight() + 4;
		if (y + 20 > screen.height - 8) {
			return;
		}
		buttons.add(new LanShareButton(last.getX(), y, last.getWidth(), last.getHeight(),
				Component.literal("管理房间"), button -> openHostPanel(screen)));
	}

	private static void openHostPanel(Screen parent) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.getSingleplayerServer() == null) {
			return;
		}
		boolean hosting = isHosting();
		a90pn.p2pc.compat.ScreenSwap.to(minecraft,
				hosting ? HostScreen.reopen(parent) : new HostUI(Component.empty(), parent));
	}

	/** 当前是否在开房间（管理器还没初始化时按「不是」处理，避免空指针）。 */
	private static boolean isHosting() {
		a90pn.p2pc.Sculk.SculkManager manager = P2PC.manager();
		return manager != null && manager.getStatus() == SculkStatues.HOST;
	}
}
