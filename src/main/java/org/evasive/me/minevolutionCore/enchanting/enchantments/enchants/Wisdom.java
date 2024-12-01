package org.evasive.me.minevolutionCore.enchanting.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.enchanting.enchantments.EnchantType;
import org.evasive.me.minevolutionCore.enchanting.enchantments.PickaxeEnchantBuilder;
import org.evasive.me.minevolutionCore.enchanting.enchantments.Rarity;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Wisdom implements PickaxeEnchantBuilder {
    @Override
    public int getMaxLevel() {
        return 3;
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
    public Rarity getRarity() {
        return Rarity.EXQUISITE;
    }
    @Override
    public NamespacedKey getKey(){
        return EnchantKeys.wisdom;
    }
}
