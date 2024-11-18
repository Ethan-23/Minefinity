package org.evasive.me.minevolutionCore.mining.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Wisdom implements PickaxeEnchantBuilder {
    @Override
    public int getExperienceCost(int level) {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "Wisdom";
    }

    @Override
    public String getSymbol() {
        return "☆";
    }

    @Override
    public String getDescription() {
        return "Increases experience dropped from breaking blocks";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.WISDOM;
    }

    @Override
    public NamespacedKey getKey(){
        return EnchantKeys.wisdom;
    }
}
