package org.evasive.me.minevolutionCore.enchanting.enchantments.rarity;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface RarityBuilder {
    TextColor getTextColor();
    Material getTierMaterial();
}
