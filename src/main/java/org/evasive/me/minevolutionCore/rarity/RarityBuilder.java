package org.evasive.me.minevolutionCore.rarity;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public class RarityBuilder {
    private final Material material;
    private final TextColor textColor;

    public RarityBuilder(Material material, TextColor textColor) {
        this.material = material;
        this.textColor = textColor;
    }

    public Material getMaterial() {
        return material;
    }

    public TextColor getTextColor() {
        return textColor;
    }
}
