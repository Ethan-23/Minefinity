package org.evasive.me.minevolutionCore.enchanting.enchantments;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface RarityBuilder {
    TextColor getTextColor();
    Material getTierMaterial();
}
