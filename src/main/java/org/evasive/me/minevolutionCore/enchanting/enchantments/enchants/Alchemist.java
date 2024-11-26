package org.evasive.me.minevolutionCore.enchanting.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.enchanting.enchantments.rarity.Rarity;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Alchemist implements PickaxeEnchantBuilder{

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "Alchemist";
    }

    @Override
    public String getSymbol() {
        return "⚛";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.ALCHEMIST;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNIQUE;
    }

    @Override
    public NamespacedKey getKey() {
        return EnchantKeys.alchemist;
    }
}
