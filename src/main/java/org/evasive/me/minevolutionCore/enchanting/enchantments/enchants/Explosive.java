package org.evasive.me.minevolutionCore.enchanting.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.enchanting.enchantments.EnchantType;
import org.evasive.me.minevolutionCore.enchanting.enchantments.PickaxeEnchantBuilder;
import org.evasive.me.minevolutionCore.enchanting.enchantments.Rarity;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Explosive implements PickaxeEnchantBuilder {
    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getName() {
        return "Explosive";
    }

    @Override
    public String getSymbol() {
        return "☢";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.EXPLOSIVE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EXQUISITE;
    }

    @Override
    public NamespacedKey getKey() {
        return EnchantKeys.explosive;
    }
}
