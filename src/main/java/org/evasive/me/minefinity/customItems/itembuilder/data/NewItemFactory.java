package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.evasive.me.minefinity.rarity.Rarity;

@FunctionalInterface
public interface NewItemFactory {
    BaseCustomItem create(String id, Material material, String name, Rarity rarity);
}

