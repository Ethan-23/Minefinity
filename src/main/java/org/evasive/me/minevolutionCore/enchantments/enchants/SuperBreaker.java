package org.evasive.me.minevolutionCore.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class SuperBreaker implements PickaxeEnchantBuilder {
    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getName() {
        return "Super Breaker";
    }

    @Override
    public String getSymbol() {
        return "⚡";
    }

    @Override
    public String getDescription() {
        return "Chance to increase mining speed for x amount of blocks for x seconds";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.SUPERBREAKER;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RADIANT;
    }

    @Override
    public NamespacedKey getKey(){
        return EnchantKeys.superbreaker;
    }
}
