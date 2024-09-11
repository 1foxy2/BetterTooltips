package dev.isxander.adaptivetooltips.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import dev.isxander.adaptivetooltips.BetterTooltips;
import dev.isxander.adaptivetooltips.helpers.ScrollTracker;
import dev.isxander.adaptivetooltips.helpers.TooltipWrapper;
import dev.isxander.adaptivetooltips.helpers.positioner.BedrockCenteringPositionModule;
import dev.isxander.adaptivetooltips.helpers.positioner.BestCornerPositionModule;
import dev.isxander.adaptivetooltips.helpers.positioner.PrioritizeTooltipTopPositionModule;
import dev.isxander.adaptivetooltips.helpers.positioner.TooltipPositionModule;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(value = GuiGraphics.class, priority = 1100)
public abstract class GuiGraphicsMixin {
    @Shadow
    public abstract PoseStack pose();

    @Shadow public abstract int guiWidth();

    @Shadow public abstract int guiHeight();

    @Shadow
    protected abstract void renderTooltipInternal(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner tooltipPositioner);

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"),cancellable = true)
    private void removeNeoforgeWrapping(Font font, List<Component> tooltipLines, Optional<TooltipComponent> visualTooltipComponent, int mouseX, int mouseY, CallbackInfo ci) {
        List<ClientTooltipComponent> list = (List<ClientTooltipComponent>)tooltipLines.stream()
                .map(Component::getVisualOrderText)
                .map(ClientTooltipComponent::create)
                .collect(Util.toMutableList());
        visualTooltipComponent.ifPresent(tooltipComponent -> list.add(list.isEmpty() ? 0 : 1, ClientTooltipComponent.create(tooltipComponent)));
        this.renderTooltipInternal(font, list, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
        ci.cancel();
    }

    // wrapping
    @ModifyVariable(method = "renderTooltipInternal", at = @At("HEAD"), argsOnly = true)
    private List<ClientTooltipComponent> modifyTooltip(List<ClientTooltipComponent> tooltip, Font font, List<ClientTooltipComponent> dontuse, int x, int y, ClientTooltipPositioner positioner) {
        return TooltipWrapper.wrapComponents(tooltip, font, this.guiWidth(), this.guiHeight(), x, positioner);
    }

    @WrapOperation(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;"))
    private Vector2ic moveTooltip(ClientTooltipPositioner positioner, int screenWidth, int screenHeight, int x, int y, int width, int height, Operation<Vector2ic> operation, Font font, List<ClientTooltipComponent> tooltip, int mouseX, int mouseY) {
        Vector2ic currentPosition = operation.call(positioner, screenWidth, screenHeight, mouseX, mouseY, width, height);

        pose().pushPose(); // injection is before matrices.push()

        if (!(positioner instanceof DefaultTooltipPositioner) && BetterTooltips.getConfig().onlyRepositionHoverTooltips.get())
            return currentPosition;

        for (TooltipPositionModule tooltipPositionModule : List.of(
                new PrioritizeTooltipTopPositionModule(),
                new BedrockCenteringPositionModule(),
                new BestCornerPositionModule()
        )) {
            Optional<Vector2ic> position = tooltipPositionModule.repositionTooltip(currentPosition.x(), currentPosition.y(), width, height, x, y, this.guiWidth(), this.guiHeight());
            if (position.isPresent())
                currentPosition = position.get();
        }

        ScrollTracker.scroll((GuiGraphics) (Object) this, tooltip, currentPosition.x(), currentPosition.y(), width, height, screenWidth, screenHeight);

        return currentPosition;
    }

    @Inject(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", ordinal = 0))
    private void closeCustomMatrices(Font textRenderer, List<ClientTooltipComponent> tooltip, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci) {
        pose().popPose();
    }

    @ModifyConstant(method = "renderTooltipInternal", constant = @Constant(intValue = 2))
    private int removeFirstLinePadding(int padding) {
        if (BetterTooltips.getConfig().removeFirstLinePadding.get())
            return 0;
        return padding;
    }
}
