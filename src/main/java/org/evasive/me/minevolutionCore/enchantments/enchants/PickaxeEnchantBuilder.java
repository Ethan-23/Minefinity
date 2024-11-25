package org.evasive.me.minevolutionCore.enchantments.enchants;

import org.bukkit.NamespacedKey;

public interface PickaxeEnchantBuilder {
    int getMaxLevel();
    String getName();
    String getSymbol();
    String getDescription();
    EnchantType getType();
    Rarity getRarity();
    NamespacedKey getKey();
}
