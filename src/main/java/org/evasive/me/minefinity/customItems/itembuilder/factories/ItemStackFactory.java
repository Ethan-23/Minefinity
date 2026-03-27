package org.evasive.me.minefinity.customItems.itembuilder.factories;

import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;

@FunctionalInterface
public interface ItemStackFactory {
    BaseCustomItem create(ItemStack itemStack);
}
