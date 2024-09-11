package dev.isxander.adaptivetooltips.helpers.positioner;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class YACLTooltipPositioner implements ClientTooltipPositioner {
    private final ScreenRectangle clickableWidget;

    public YACLTooltipPositioner(ScreenRectangle clickableWidget) {
        this.clickableWidget = clickableWidget;
    }

    @Override
    public @NotNull Vector2ic positionTooltip(int screenWidth, int screenHeight, int x, int y, int width, int height) {
        int centerX = clickableWidget.left() + clickableWidget.width() / 2;
        int aboveY = clickableWidget.top() - height - 4;
        int belowY = clickableWidget.top() + clickableWidget.height() + 4;

        int obstructionBottom = (belowY + height) - screenHeight;
        int obstructionTop = -aboveY;

        int yResult = belowY;
        if (obstructionBottom > 0)
            yResult = obstructionBottom > obstructionTop ? aboveY : belowY;

        int xResult = Mth.clamp(centerX - width / 2, -4, screenWidth - width - 4);

        return new Vector2i(xResult, yResult);
    }
}
