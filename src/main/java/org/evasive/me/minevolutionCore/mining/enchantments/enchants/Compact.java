package org.evasive.me.minevolutionCore.mining.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Compact implements PickaxeEnchantBuilder {
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
        return "Compact";
    }

    @Override
    public String getSymbol() {
        return "⚙";
    }

    @Override
    public String getDescription() {
        return "Increase chance of dropping enchanted materials";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.COMPACT;
    }

    @Override
    public NamespacedKey getKey(){
        return EnchantKeys.compact;
    }


}
