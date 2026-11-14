package a90pn.p2pc.client;

import a90pn.p2pc.P2PC;
import a90pn.p2pc.Sculk.SculkStatues;
import a90pn.p2pc.client.ui.HostScreen;
import a90pn.p2pc.client.ui.HostUI;
import a90pn.p2pc.client.ui.JoinUI;
import a90pn.p2pc.client.ui.LanShareButton;
import a90pn.p2pc.client.ui.PearlButton;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public class P2PCClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		// 离开服务器/世界时把隧道收掉：否则管理器会一直开着 watch，
		// 连接状态日志刷个不停（连接被拒、断线、退出世界都会走到这里）。
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			P2PC mod = P2PC.getInstance();
			if (mod != null && mod.getSculkManager() != null) {
				mod.getSculkManager().close();
			}
		});

		// 右上角网络状态队列：只在「加入别人世界」时显示（加载中 / 备用节点 / 高延迟）
		HudRenderCallback.EVENT.register(new NetworkStatusHud());

		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen) {
				List<AbstractWidget> buttons = Screens.getButtons(screen);
				// 窗口缩放/重新 init 时 AFTER_INIT 会再次触发，避免叠加出第二个按钮。
				if (buttons.stream().anyMatch(widget -> widget instanceof PearlButton)) {
					return;
				}
				// 贴在按钮列的右侧、和第二个按钮（多人游戏）同一行，和原版布局保持一条线。
				AbstractWidget anchor = buttons.size() > 1 ? buttons.get(1) : null;
				int x = anchor != null ? anchor.getX() + anchor.getWidth() + 8 : scaledWidth - 40;
				int y = anchor != null ? anchor.getY() : scaledHeight / 2 - 10;
				buttons.add(new PearlButton(x, y, Component.empty(), Component.literal("加入世界"),
						button -> Minecraft.getInstance().setScreen(new JoinUI(Component.empty(), screen))));
			} else if (screen instanceof PauseScreen && Minecraft.getInstance().getSingleplayerServer() != null) {
				replaceShareToLanButton(screen);
			}
		});
	}

	/**
	 * 把原版「对局域网开放」按钮换成我们的入口：原位、原文字，点开是带 P2PC 选项的创建房间页。
	 *
	 * <p>注意：世界已经开放到局域网后，原版会把槽位换成「举报玩家」（isPublished 分支），
	 * 所以两种文字都要认；此时按钮文字换成「管理房间」，点开是房间管理页。</p>
	 */
	private static void replaceShareToLanButton(net.minecraft.client.gui.screens.Screen screen) {
		List<AbstractWidget> buttons = Screens.getButtons(screen);
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
				boolean hosting = P2PC.getInstance().getSculkManager().getStatus() == SculkStatues.HOST;
				Component label = hosting ? Component.literal("管理房间") : shareLabel;
				buttons.add(new LanShareButton(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(),
						label, button -> openHostPanel(screen)));
				return;
			}
		}
	}

	private static void openHostPanel(net.minecraft.client.gui.screens.Screen parent) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.getSingleplayerServer() == null) {
			return;
		}
		boolean hosting = P2PC.getInstance().getSculkManager().getStatus() == SculkStatues.HOST;
		minecraft.setScreen(hosting ? HostScreen.reopen(parent) : new HostUI(Component.empty(), parent));
	}
}
