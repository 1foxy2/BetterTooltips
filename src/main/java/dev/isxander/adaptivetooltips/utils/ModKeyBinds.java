package dev.isxander.adaptivetooltips.utils;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import dev.isxander.adaptivetooltips.BetterTooltips;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT, modid = BetterTooltips.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModKeyBinds {
    public static final String KEY_CATEGORY_BETTER_TOOLTIPS = "key.category.better_tooltips";

    public static final Lazy<KeyMapping> SCROLL_KEY = Lazy.of(() -> new KeyMapping("key.better_tooltips.scroll_key",
            KeyConflictContext.GUI, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, KEY_CATEGORY_BETTER_TOOLTIPS));
    public static final Lazy<KeyMapping> HORIZONTAL_SCROLL_KEY = Lazy.of(() -> new KeyMapping("key.better_tooltips.horizontal_scroll_key",
            KeyConflictContext.GUI, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_CONTROL, KEY_CATEGORY_BETTER_TOOLTIPS));

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(ModKeyBinds.SCROLL_KEY.get());
        event.register(ModKeyBinds.HORIZONTAL_SCROLL_KEY.get());
    }
}
