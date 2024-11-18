package org.evasive.me.minevolutionCore.mining.enchantments.enchants;

import org.evasive.me.minevolutionCore.mining.blocks.block.BlockBuilder;

public enum EnchantType {
    EFFICIENCY(new Efficiency()),
    FORTUNE(new Fortune()),
    COMPACT(new Compact()),
    WISDOM(new Wisdom()),
    SUPERBREAKER(new SuperBreaker())
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
