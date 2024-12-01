package org.evasive.me.minevolutionCore.enchanting.enchantments.rarity;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.evasive.me.minevolutionCore.enchanting.enchantments.RarityBuilder;

public class Mythic implements RarityBuilder {
    @Override
    public TextColor getTextColor() {
        return TextColor.fromHexString("#ff435c");
    }

    @Override
    public Material getTierMaterial() {
        return Material.RED_DYE;
    }
}
