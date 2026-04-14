package com.lx862.mtrscripting.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;

public class VanillaTextWrapper {
    private final MutableComponent impl;
    private Style style;
    private boolean skipApplyingStyle;

    public VanillaTextWrapper(MutableComponent impl) {
        this.impl = impl;
        this.style = Style.EMPTY;
    }

    public static VanillaTextWrapper literal(String str) {
        return new VanillaTextWrapper(Component.literal(str));
    }

    public static VanillaTextWrapper translatable(String str, Object... placeholders) {
        return new VanillaTextWrapper(Component.translatable(str, placeholders));
    }

    public VanillaTextWrapper append(VanillaTextWrapper other) {
        this.impl.append(other.impl());
        skipApplyingStyle = true;
        return this;
    }

    public VanillaTextWrapper withBold() {
        this.style = this.style.withBold(true);
        return this;
    }

    public VanillaTextWrapper withItalic() {
        this.style = this.style.withItalic(true);
        return this;
    }

    public VanillaTextWrapper withFont(String id) {
        this.style = this.style.withFont(ResourceLocation.parse(id));
        return this;
    }

    public VanillaTextWrapper withColor(int rgb) {
        this.style = this.style.withColor(TextColor.fromRgb(rgb));
        return this;
    }

    public VanillaTextWrapper withColor(String colorName) {
        ChatFormatting formatting = ChatFormatting.getByName(colorName);
        if (formatting == null) {
            throw new IllegalArgumentException("Color " + colorName + " is not a valid text color!");
        }
        this.style = this.style.withColor(formatting);
        return this;
    }

    public String getString() {
        return this.impl.getString();
    }

    public MutableComponent impl() {
        return skipApplyingStyle ? this.impl : this.impl.copy().setStyle(this.style);
    }
}
