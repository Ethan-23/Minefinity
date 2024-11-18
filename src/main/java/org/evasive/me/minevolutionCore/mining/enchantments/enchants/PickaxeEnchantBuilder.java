package org.evasive.me.minevolutionCore.mining.enchantments.enchants;

import org.bukkit.NamespacedKey;

public interface PickaxeEnchantBuilder {
    int getExperienceCost(int level);
    int getMaxLevel();
    String getName();
    String getSymbol();
    String getDescription();
    EnchantType getType();
    NamespacedKey getKey();
}
