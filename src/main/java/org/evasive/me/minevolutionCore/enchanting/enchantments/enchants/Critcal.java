package org.evasive.me.minevolutionCore.enchanting.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.enchanting.enchantments.rarity.Rarity;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Critcal implements PickaxeEnchantBuilder{
    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "Critical";
    }

    @Override
    public String getSymbol() {
        return "¤";
    }

    @Override
    public String getDescription() {
        return "Spawns particles on the block that increase your mining speed when looking at";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.CRITICAL;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MINOR;
    }

    @Override
    public NamespacedKey getKey() {
        return EnchantKeys.critical;
    }
}
