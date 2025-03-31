package dev.isxander.adaptivetooltips.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import dev.isxander.adaptivetooltips.BetterTooltips;
import dev.isxander.adaptivetooltips.config.BetterTooltipsConfig;
import dev.isxander.adaptivetooltips.helpers.TooltipWrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {
    @Inject(
            method = "gatherTooltipComponentsFromElements",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/client/event/RenderTooltipEvent$GatherComponents;" +
                            "getTooltipElements()Ljava/util/List;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private static void replaceWrapper(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements,
                                       int mouseX, int screenWidth, int screenHeight, Font fallbackFont,
                                       CallbackInfoReturnable<List<ClientTooltipComponent>> cir,
                                       @Local RenderTooltipEvent.GatherComponents event) {
        if (BetterTooltips.getConfig().wrapText.get() != BetterTooltipsConfig.WrapText.OFF) {
            cir.setReturnValue(event.getTooltipElements().stream()
                    .map(either -> either.map(
                            text -> ClientTooltipComponent.create(text instanceof Component ?
                                    ((Component) text).getVisualOrderText() : Language.getInstance().getVisualOrder(text)),
                            ClientTooltipComponent::create))
                    .toList());
        }
    }
}
