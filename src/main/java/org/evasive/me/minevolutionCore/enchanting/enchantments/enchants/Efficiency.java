package org.evasive.me.minevolutionCore.enchanting.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.enchanting.enchantments.EnchantType;
import org.evasive.me.minevolutionCore.enchanting.enchantments.PickaxeEnchantBuilder;
import org.evasive.me.minevolutionCore.enchanting.enchantments.Rarity;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Efficiency implements PickaxeEnchantBuilder {

    @Override
    public int getMaxLevel() {
        return 5;
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
    public Rarity getRarity() {
        return Rarity.MINOR;
    }
    @Override
    public NamespacedKey getKey(){
        return EnchantKeys.efficiency;
    }
}
