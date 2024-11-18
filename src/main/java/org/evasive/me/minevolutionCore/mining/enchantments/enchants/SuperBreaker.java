package org.evasive.me.minevolutionCore.mining.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class SuperBreaker implements PickaxeEnchantBuilder {
    @Override
    public int getExperienceCost(int level) {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 100;
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
    public NamespacedKey getKey(){
        return EnchantKeys.superbreaker;
    }
}
