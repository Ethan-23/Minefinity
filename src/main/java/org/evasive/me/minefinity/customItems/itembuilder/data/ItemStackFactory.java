package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemStackFactory {
    BaseCustomItem create(ItemStack itemStack);
}
