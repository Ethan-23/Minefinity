package org.evasive.me.minefinity.forge.data;

import org.bukkit.Material;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;

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
        return new ItemBuilder(material, TextConversions.parse("<yellow>" + name + "</yellow>"));
    }
}
