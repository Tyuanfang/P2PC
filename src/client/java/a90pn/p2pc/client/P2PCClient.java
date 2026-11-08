package a90pn.p2pc.client;

import a90pn.p2pc.client.ui.JoinUI;
import a90pn.p2pc.client.ui.PearlButton;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public class P2PCClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen) {
				Screens.getButtons(screen).add(new PearlButton(scaledWidth/5,scaledHeight/2,20,20,Component.empty(),button -> Minecraft.getInstance().setScreen(new JoinUI(Component.empty(),screen))));
			}
		});
	}
}