package org.evasive.me.minevolutionCore.enchanting.enchantments.rarity;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public class Pristine implements RarityBuilder{
    @Override
    public TextColor getTextColor() {
        return TextColor.fromHexString("#FFAA00");
    }

    @Override
    public Material getTierMaterial() {
        return Material.ORANGE_DYE;
    }
}
