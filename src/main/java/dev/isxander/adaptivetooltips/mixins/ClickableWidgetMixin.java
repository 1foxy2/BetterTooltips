package dev.isxander.adaptivetooltips.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.adaptivetooltips.BetterTooltips;
import dev.isxander.adaptivetooltips.helpers.positioner.YACLTooltipPositioner;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WidgetTooltipHolder.class)
public class ClickableWidgetMixin {
    @ModifyReturnValue(method = "createTooltipPositioner", at = @At("RETURN"))
    private ClientTooltipPositioner changePositioner(ClientTooltipPositioner tooltipPositioner, ScreenRectangle screenRectangle, boolean hovering, boolean focused) {
        if (BetterTooltips.getConfig().useYACLTooltipPositioner.get())
            return new YACLTooltipPositioner(screenRectangle);
        return tooltipPositioner;
    }
}
