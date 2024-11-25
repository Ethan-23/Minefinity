package org.evasive.me.minevolutionCore.enchantments.enchants;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

public class Fortune implements PickaxeEnchantBuilder {

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getName() {
        return "Fortune";
    }

    @Override
    public String getSymbol() {
        return "☘";
    }

    @Override
    public String getDescription() {
        return "Increases chance of getting extra block drop";
    }

    @Override
    public EnchantType getType() {
        return EnchantType.FORTUNE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNIQUE;
    }

    @Override
    public NamespacedKey getKey(){
        return EnchantKeys.fortune;
    }
}
