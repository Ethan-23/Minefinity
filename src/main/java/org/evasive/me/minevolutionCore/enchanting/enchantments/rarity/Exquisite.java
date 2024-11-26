package org.evasive.me.minevolutionCore.enchanting.enchantments.rarity;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public class Exquisite implements RarityBuilder{
    @Override
    public TextColor getTextColor() {
        return TextColor.fromHexString("#AA00AA");
    }

    @Override
    public Material getTierMaterial() {
        return Material.PURPLE_DYE;
    }
}
