package a90pn.p2pc;

import a90pn.p2pc.Sculk.SculkManager;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P2PC implements ModInitializer {
	public static final String MOD_ID = "p2pc";
	public SculkManager sculkManager;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static P2PC instance ;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Initializing P2PC");
		instance = this;
		sculkManager = new SculkManager();

	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public SculkManager getSculkManager() {
		return sculkManager;
	}
	public static P2PC getInstance() {
		return instance;
	}
}
