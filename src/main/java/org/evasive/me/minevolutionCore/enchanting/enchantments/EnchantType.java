package org.evasive.me.minevolutionCore.enchanting.enchantments;

import org.evasive.me.minevolutionCore.enchanting.enchantments.enchants.*;

public enum EnchantType {
    CRITICAL(new Critcal()),
    EFFICIENCY(new Efficiency()),
    ALCHEMIST(new Alchemist()),
    FORTUNE(new Fortune()),
    SUPERBREAKER(new SuperBreaker()),
    EXPLOSIVE(new Explosive()),
    WISDOM(new Wisdom()),
    COMPACT(new Compact()),
    ORBITALMINER(new OrbitalMiner())
    ;

    private final PickaxeEnchantBuilder pickaxeEnchantBuilder;

    // Constructor for BlockType enum
    EnchantType(PickaxeEnchantBuilder pickaxeEnchantBuilder) {
        this.pickaxeEnchantBuilder = pickaxeEnchantBuilder;
    }

    public PickaxeEnchantBuilder getPickaxeEnchantBuilder() {
        return pickaxeEnchantBuilder;
    }
}
