package org.evasive.me.minefinity.customItems.itembuilder.factories;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;

@FunctionalInterface
public interface NewItemFactory {
    BaseCustomItem create(String id, Material material, String name, Rarity rarity);
}

