package org.evasive.me.minevolutionCore.enchanting.enchantments.rarity;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public class Unique implements RarityBuilder{
    @Override
    public TextColor getTextColor() {
        return TextColor.fromHexString("#55FF55");
    }

    @Override
    public Material getTierMaterial() {
        return Material.LIME_DYE;
    }
}
