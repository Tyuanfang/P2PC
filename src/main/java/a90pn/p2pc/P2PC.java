package a90pn.p2pc;

import a90pn.p2pc.Sculk.SculkManager;
import a90pn.p2pc.compat.Textures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if fabric {
public class P2PC implements net.fabricmc.api.ModInitializer {
	@Override
	public void onInitialize() {
		P2PC.bootstrap();
	}
//?} else {
/*//? if neoforge {
/^@net.neoforged.fml.common.Mod("p2pc")
public class P2PC {
	public P2PC() {
		P2PC.bootstrap();
		// 客户端钩子（界面注入、HUD、tick）只在客户端加载器里注册。
		//? if <1.21.11 {
		if (net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT) {
		//?} else {
		/^if (net.neoforged.fml.loading.FMLEnvironment.getDist() == net.neoforged.api.distmarker.Dist.CLIENT) {
		^///?}
			a90pn.p2pc.client.P2PCClient.init();
		}
	}
^///?} else {
@net.minecraftforge.fml.common.Mod("p2pc")
public class P2PC {
	//? if <1.21.11 {
	// Forge 1.21.1 的 GUI 覆盖层事件挂在 mod 总线上，只有构造器能拿到 IEventBus。
	public P2PC(net.minecraftforge.eventbus.api.IEventBus modBus) {
		P2PC.bootstrap();
		if (net.minecraftforge.fml.loading.FMLEnvironment.dist == net.minecraftforge.api.distmarker.Dist.CLIENT) {
			a90pn.p2pc.client.P2PCClient.init();
			a90pn.p2pc.client.P2PCClient.initForgeModBus(modBus);
		}
	}
	//?} else {
	/^public P2PC() {
		P2PC.bootstrap();
		if (net.minecraftforge.fml.loading.FMLEnvironment.dist == net.minecraftforge.api.distmarker.Dist.CLIENT) {
			a90pn.p2pc.client.P2PCClient.init();
		}
	}
	^///?}
//?}
*///?}
	public static final String MOD_ID = "p2pc";
	public SculkManager sculkManager;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static P2PC instance;

	{
		// 每个加载器都会实例化这个类（Fabric 的入口 / Forge、NeoForge 的 @Mod 类），
		// 实例化时就把单例挂上，getInstance() 在后续任何阶段都可用。
		instance = this;
	}

	/** 通用初始化：与加载器无关的部分。 */
	static void bootstrap() {
		LOGGER.info("Initializing P2PC");
		if (instance == null) {
			return;
		}
		instance.sculkManager = new SculkManager();
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
