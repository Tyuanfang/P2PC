package a90pn.p2pc.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class P2PCClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen) {

				// --- 按钮代码开始 ---
				Button pearlButton = new Button(
						10, 50, 20, 20,
						Component.empty(),
						button -> {

                        },
						null
				) {
					@Override
					protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
						super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
						guiGraphics.renderItem(new ItemStack(Items.ENDER_PEARL), this.getX() + 2, this.getY() + 2);
					}
				};

				Screens.getButtons(screen).add(pearlButton);
			}
		});
	}
}