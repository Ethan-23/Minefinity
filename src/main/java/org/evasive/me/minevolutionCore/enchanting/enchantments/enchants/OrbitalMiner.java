package org.evasive.me.minevolutionCore.enchanting.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.enchanting.enchantments.EnchantType;
import org.evasive.me.minevolutionCore.enchanting.enchantments.PickaxeEnchantBuilder;
import org.evasive.me.minevolutionCore.enchanting.enchantments.Rarity;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class OrbitalMiner implements PickaxeEnchantBuilder {
    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "Orbital Miner";
    }

    @Override
    public String getSymbol() {
        return "☄";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.ORBITALMINER;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.PRISTINE;
    }

    @Override
    public NamespacedKey getKey() {
        return EnchantKeys.orbitalminer;
    }
}
