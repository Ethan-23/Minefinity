package org.evasive.me.minefinity.forge.data;

import org.bukkit.Material;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.Messages;

public enum ForgeCategories {
    MATERIALS(Material.COBBLESTONE, "Materials"),
    COMPONENTS(Material.NETHER_STAR, "Components"),
    PICKAXES(Material.WOODEN_PICKAXE, "Pickaxes"),
    STORAGE(Material.PLAYER_HEAD, "Storage");

    private final Material material;
    private final String name;

    ForgeCategories(Material material, String name) {
        this.material = material;
        this.name = name;
    }

    public ItemBuilder toItemBuilder() {
        return new ItemBuilder(material, Messages.parse("<yellow>" + name + "</yellow>"));
    }
}
