package org.evasive.me.minefinity.towns.structures.forge.blacksmith.data;

import org.bukkit.Material;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;

public enum ForgeCategories {
    PICKAXE_TEMPLATES(Material.IRON_PICKAXE, "Pickaxe Templates"),
    PICKAXE_HEADS(Material.IRON_INGOT, "Pickaxe Heads"),
    PICKAXE_CORES(Material.NETHER_STAR, "Pickaxe Cores"),
    PICKAXE_HANDLES(Material.STICK, "Pickaxe Handles");

    private final Material material;
    private final String name;

    ForgeCategories(Material material, String name) {
        this.material = material;
        this.name = name;
    }

    public ItemBuilder toItemBuilder() {
        return new ItemBuilder(material, "<yellow>" + name + "</yellow>");
    }
}
