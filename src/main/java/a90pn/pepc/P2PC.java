package a90pn.pepc;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P2PC implements ModInitializer {
	public static final String MOD_ID = "p2pc";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		try {
			SculkClient.ensureLoaded();
			LOGGER.info("[p2pc] sculk FFI 原生库已加载（MC 端口 {}，入站端口 {}）",
					SculkClient.defaultMcPort(), SculkClient.defaultInletPort());
		} catch (Throwable error) {
			LOGGER.error("[p2pc] sculk FFI 原生库加载失败", error);
		}
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
