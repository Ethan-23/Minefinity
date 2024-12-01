package org.evasive.me.minevolutionCore.enchanting.enchantments;

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
