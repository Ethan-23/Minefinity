package org.evasive.me.minevolutionCore.enchanting.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.enchanting.enchantments.rarity.Rarity;

public interface PickaxeEnchantBuilder {
    int getMaxLevel();
    String getName();
    String getSymbol();
    String getDescription();
    EnchantType getType();
    Rarity getRarity();
    NamespacedKey getKey();
}
