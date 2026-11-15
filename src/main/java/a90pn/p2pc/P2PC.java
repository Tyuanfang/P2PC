package a90pn.p2pc;

import a90pn.p2pc.Sculk.SculkManager;
import a90pn.p2pc.compat.Textures;
import net.fabricmc.api.ModInitializer;

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
		LOGGER.info("Initializing P2PC，ZhgnNo is gay！");
		instance = this;
		sculkManager = new SculkManager();

	}

	/** 生成 {@code p2pc:<path>} 的资源 id（返回 Object 以跨版本兼容 ResourceLocation / Identifier）。 */
	public static Object id(String path) {
		return Textures.of(MOD_ID, path);
	}

	public SculkManager getSculkManager() {
		return sculkManager;
	}
	public static P2PC getInstance() {
		return instance;
	}
}
