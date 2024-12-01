package org.evasive.me.minevolutionCore.enchanting.enchantments.rarity;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.evasive.me.minevolutionCore.enchanting.enchantments.RarityBuilder;

public class Radiant implements RarityBuilder {
    @Override
    public TextColor getTextColor() {
        return TextColor.fromHexString("#00FFFF");
    }

    @Override
    public Material getTierMaterial() {
        return Material.CYAN_DYE;
    }
}
