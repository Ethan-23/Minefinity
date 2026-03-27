package org.evasive.me.minefinity.core.rarity;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public enum Rarity {
    MINOR(Material.GRAY_DYE, TextColor.fromHexString("#BFBFBF")),
    UNIQUE(Material.LIME_DYE, TextColor.fromHexString("#55FF55")),
    RADIANT(Material.CYAN_DYE, TextColor.fromHexString("#00FFFF")),
    EXQUISITE(Material.PURPLE_DYE, TextColor.fromHexString("#AA00AA")),
    PRISTINE(Material.ORANGE_DYE, TextColor.fromHexString("#FFAA00")),
    MYTHIC(Material.RED_DYE, TextColor.fromHexString("#FF435C"));

    private final RarityBuilder rarityBuilder;

    // Constructor for BlockType enum
    Rarity(Material material, TextColor textColor) {
        this.rarityBuilder = new RarityBuilder(material, textColor);
    }

    public RarityBuilder getRarityBuilder() {
        return rarityBuilder;
    }

}


