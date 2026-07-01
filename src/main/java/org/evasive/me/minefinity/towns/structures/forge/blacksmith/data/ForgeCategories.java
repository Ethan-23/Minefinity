package org.evasive.me.minefinity.towns.structures.forge.blacksmith.data;

import org.bukkit.Material;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;

public enum ForgeCategories {
    PICKAXES(Material.IRON_PICKAXE, "Pickaxe Templates"),
    TOOL_HEADS(Material.IRON_INGOT, "Tool Heads"),
    TOOL_CORES(Material.NETHER_STAR, "Tool Cores"),
    TOOL_HANDLES(Material.STICK, "Tool Handles");

    private final Material material;
    private final String name;

    ForgeCategories(Material material, String name) {
        this.material = material;
        this.name = name;
    }

    public CustomItemBuilder toItemBuilder() {
        return new CustomItemBuilder(material, "<yellow>" + name + "</yellow>");
    }
}
