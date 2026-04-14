package com.lx862.mtrscripting.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemStackWrapper {
    private final ItemStack itemStack;

    public ItemStackWrapper(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String itemId() {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(this.itemStack.getItem());
        return id.getNamespace() + ":" + id.getPath();
    }

    public String translationId() {
        return this.itemStack.getDescriptionId();
    }

    public boolean empty() {
        return this.itemStack.isEmpty();
    }

    public int count() {
        return this.itemStack.getCount();
    }
}
