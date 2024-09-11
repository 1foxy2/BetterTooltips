package dev.isxander.adaptivetooltips;

import dev.isxander.adaptivetooltips.config.BetterTooltipsConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@Mod(dist = Dist.CLIENT, value = BetterTooltips.MOD_ID)
public class BetterTooltips {
    public static final Pair<BetterTooltipsConfig, ModConfigSpec> CONFIG;
    public static final String MOD_ID = "better_tooltips";

    static {
        CONFIG = new ModConfigSpec.Builder()
                .configure(BetterTooltipsConfig::new);
    }

    public BetterTooltips(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, CONFIG.getValue());
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static BetterTooltipsConfig getConfig() {
        return CONFIG.getLeft();
    }
}
