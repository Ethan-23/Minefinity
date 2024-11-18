package org.evasive.me.minevolutionCore.mining.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Efficiency implements PickaxeEnchantBuilder {
    @Override
    public int getExperienceCost(int level) {
        //Equation to figure out cost
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "Efficiency";
    }

    @Override
    public String getSymbol() {
        return "⛏";
    }

    @Override
    public String getDescription() {
        return "Increase the mining speed of your pickaxe";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.EFFICIENCY;
    }

    @Override
    public NamespacedKey getKey(){
        return EnchantKeys.efficiency;
    }
}
