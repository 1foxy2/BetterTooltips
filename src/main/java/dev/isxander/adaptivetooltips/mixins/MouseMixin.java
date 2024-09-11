package dev.isxander.adaptivetooltips.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import dev.isxander.adaptivetooltips.helpers.ScrollTracker;
import dev.isxander.adaptivetooltips.utils.ModKeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Shadow @Final private Minecraft minecraft;

    @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z"))
    private boolean trackMouseWheel(Screen instance, double mouseX, double mouseY, double scrollX, double scrollY, Operation<Boolean> original) {
        if (InputConstants.isKeyDown(minecraft.getWindow().getWindow(),ModKeyBinds.SCROLL_KEY.get().getKey().getValue())) {
            if (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), ModKeyBinds.HORIZONTAL_SCROLL_KEY.get().getKey().getValue())) {
                ScrollTracker.addHorizontalScroll((int) Math.signum(scrollY));
            } else {
                ScrollTracker.addVerticalScroll((int) Math.signum(scrollY));
                ScrollTracker.addHorizontalScroll((int) Math.signum(scrollX));
            }
            return false;
        }

        return original.call(instance, mouseX, mouseY, scrollX, scrollY);
    }
}
