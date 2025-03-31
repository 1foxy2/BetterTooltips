package dev.isxander.adaptivetooltips.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.adaptivetooltips.BetterTooltips;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(TooltipRenderUtil.class)
public class TooltipRenderUtilMixin {
    @Inject(
            method = "renderTooltipBackground(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIII)V",
            at = @At("HEAD")
    )
    private static void changeBackgroundColor(GuiGraphics guiGraphics, int x, int y,
                                              int width, int height, int z,
                                              int backgroundTop, int backgroundBottom,
                                              int borderTop, int borderBottom, CallbackInfo ci,
                                              @Local(ordinal = 5, argsOnly = true) LocalIntRef localBackgroundTop,
                                              @Local(ordinal = 6, argsOnly = true) LocalIntRef localBackgroundBottom,
                                              @Local(ordinal = 7, argsOnly = true) LocalIntRef localBorderTop,
                                              @Local(ordinal = 8, argsOnly = true) LocalIntRef localBorderBottom) {
        changeAlpha(localBackgroundTop);
        changeAlpha(localBackgroundBottom);
        changeAlpha(localBorderTop);
        changeAlpha(localBorderBottom);
    }

    private static void changeAlpha(LocalIntRef color) {
        Color prevColor = new Color(color.get(), true);
        color.set(new Color(
                prevColor.getRed(),
                prevColor.getGreen(),
                prevColor.getBlue(),
                (int) Mth.clamp(
                        prevColor.getAlpha() * BetterTooltips.getConfig().tooltipTransparency.get(),
                        0, 255
                )
        ).getRGB());
    }
}
