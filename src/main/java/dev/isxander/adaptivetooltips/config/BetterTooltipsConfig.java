package dev.isxander.adaptivetooltips.config;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.TranslatableEnum;

public class BetterTooltipsConfig {
    public final ModConfigSpec.BooleanValue overwriteVanillaWrapping;
    public final ModConfigSpec.BooleanValue prioritizeTooltipTop;
    public final ModConfigSpec.BooleanValue bedrockCentering;
    public final ModConfigSpec.BooleanValue bestCorner;
    public final ModConfigSpec.BooleanValue alwaysBestCorner;
    public final ModConfigSpec.BooleanValue preventVanillaClamping;
    public final ModConfigSpec.BooleanValue onlyRepositionHoverTooltips;
    public final ModConfigSpec.BooleanValue smoothScrolling;
    public final ModConfigSpec.IntValue verticalScrollSensitivity;
    public final ModConfigSpec.IntValue horizontalScrollSensitivity;
    public final ModConfigSpec.DoubleValue tooltipTransparency;
    public final ModConfigSpec.BooleanValue removeFirstLinePadding;
    public final ModConfigSpec.BooleanValue useYACLTooltipPositioner;
    public final ModConfigSpec.EnumValue<WrapText> wrapText;
    public final ModConfigSpec.EnumValue<ScrollDirection> scrollDirection;

    public BetterTooltipsConfig(ModConfigSpec.Builder builder) {
        builder.push("content_manipulation");
        wrapText = builder
                .defineEnum("wrapText", WrapText.SCREEN_WIDTH);
        overwriteVanillaWrapping = builder
                .define("overwriteVanillaWrapping", false);
        builder.pop();

        builder.push("positioning");
        prioritizeTooltipTop = builder
                .define("prioritizeTooltipTop", true);
        bedrockCentering = builder
                .define("bedrockCentering", true);
        bestCorner = builder
                .define("bestCorner", false);
        alwaysBestCorner = builder
                .define("alwaysBestCorner", false);
        preventVanillaClamping = builder
                .define("preventVanillaClamping", true);
        onlyRepositionHoverTooltips = builder
                .define("onlyRepositionHoverTooltips", true);
        useYACLTooltipPositioner = builder
                .define("useYACLTooltipPositioner", false);
        builder.pop();

        builder.push("scrolling");
        smoothScrolling = builder
                .define("smoothScrolling", true);
        scrollDirection = builder
                .defineEnum("scrollDirection", ScrollDirection.NATURAL);
        verticalScrollSensitivity = builder
                .defineInRange("verticalScrollSensitivity", 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
        horizontalScrollSensitivity = builder
                .defineInRange("horizontalScrollSensitivity", 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
        builder.pop();

        builder.push("style");
        tooltipTransparency = builder
                .defineInRange("tooltipTransparency", 1d, 0, 1);
        removeFirstLinePadding = builder
                .define("removeFirstLinePadding", true);
        builder.pop();
    }

    public enum WrapText implements TranslatableEnum {
        OFF,
        SCREEN_WIDTH,
        REMAINING_WIDTH,
        HALF_SCREEN_WIDTH,
        SMART;

        @Override
        public Component getTranslatedName() {
            return Component.translatable("better_tooltips.wrap_text_behaviour." + this.name().toLowerCase());
        }
    }

    public enum ScrollDirection implements TranslatableEnum {
        NATURAL,
        REVERSE;

        @Override
        public Component getTranslatedName() {
            return Component.translatable("better_tooltips.scroll_direction." + this.name().toLowerCase());
        }
    }
}
