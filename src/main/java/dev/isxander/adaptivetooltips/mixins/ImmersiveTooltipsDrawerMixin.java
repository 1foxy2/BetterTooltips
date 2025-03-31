package dev.isxander.adaptivetooltips.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.adaptivetooltips.BetterTooltips;
import dev.isxander.adaptivetooltips.helpers.ScrollTracker;
import dev.isxander.adaptivetooltips.helpers.TooltipWrapper;
import dev.isxander.adaptivetooltips.helpers.positioner.BedrockCenteringPositionModule;
import dev.isxander.adaptivetooltips.helpers.positioner.BestCornerPositionModule;
import dev.isxander.adaptivetooltips.helpers.positioner.PrioritizeTooltipTopPositionModule;
import dev.isxander.adaptivetooltips.helpers.positioner.TooltipPositionModule;
import io.github.yanggx98.immersive.tooltip.ImmersiveTooltipDrawer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(value = ImmersiveTooltipDrawer.class, priority = 1100)
public class ImmersiveTooltipsDrawerMixin {

    // wrapping
    @Inject(method = "drawTooltip", at = @At("HEAD"))
    private void modifyTooltip(GuiGraphics guiGraphics, Font font, List<ClientTooltipComponent> tooltip, int x, int y,
                               ClientTooltipPositioner positioner, CallbackInfo ci,
                               @Local LocalRef<List<ClientTooltipComponent>> listLocalRef) {
        listLocalRef.set(TooltipWrapper.wrapComponents(tooltip, font, guiGraphics.guiWidth(), guiGraphics.guiHeight(), x, positioner));
    }

    @WrapOperation(method = "drawTooltip", require = 1,at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;"))
    private Vector2ic moveTooltip(ClientTooltipPositioner positioner, int screenWidth, int screenHeight,
                                  int x, int y, int width, int height, Operation<Vector2ic> operation,
                                  @Local(argsOnly = true) Font font, @Local(argsOnly = true) List<ClientTooltipComponent> tooltip,
                                  @Local(argsOnly = true) GuiGraphics guiGraphics) {
        Vector2ic currentPosition = operation.call(positioner, screenWidth, screenHeight, x, y, width, height);

        guiGraphics.pose().pushPose(); // injection is before matrices.push()

        if (!(positioner instanceof DefaultTooltipPositioner) && BetterTooltips.getConfig().onlyRepositionHoverTooltips.get())
            return currentPosition;

        for (TooltipPositionModule tooltipPositionModule : List.of(
                new PrioritizeTooltipTopPositionModule(),
                new BedrockCenteringPositionModule(),
                new BestCornerPositionModule()
        )) {
            Optional<Vector2ic> position = tooltipPositionModule.repositionTooltip(currentPosition.x(), currentPosition.y(), width, height, x, y, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            if (position.isPresent())
                currentPosition = position.get();
        }

        ScrollTracker.scroll(guiGraphics, tooltip, currentPosition.x(), currentPosition.y(), width, height, screenWidth, screenHeight);

        return currentPosition;
    }

    @Inject(method = "drawTooltip", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", ordinal = 0))
    private void closeCustomMatrices(GuiGraphics guiGraphics, Font textRenderer, List<ClientTooltipComponent> tooltip, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci) {
        guiGraphics.pose().popPose();
    }
}
