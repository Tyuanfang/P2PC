package a90pn.p2pc.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Mod Menu 入口：让模组在「模组列表 → P2PC → 配置」里有一个设置页。
 *
 * <p>Mod Menu 是可选依赖——没装时这个类不会被加载，模组其他部分照常工作。</p>
 */
public final class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return a90pn.p2pc.client.ui.P2PCConfigScreen::new;
    }
}
