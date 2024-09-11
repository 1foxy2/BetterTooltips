package dev.isxander.adaptivetooltips.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WidgetTooltipHolder.class)
public class ClickableWidgetMixin {
    @ModifyReturnValue(method = "createTooltipPositioner", at = @At("RETURN"))
    private ClientTooltipPositioner changePositioner(ClientTooltipPositioner tooltipPositioner) {
        //if (BetterTooltips.getConfig().useYACLTooltipPositioner)
        //    return new YACLTooltipPositioner((AbstractWidget) (Object) this);
        return tooltipPositioner;
    }
}
