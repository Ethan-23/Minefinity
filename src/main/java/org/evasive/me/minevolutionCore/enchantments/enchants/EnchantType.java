package org.evasive.me.minevolutionCore.enchantments.enchants;

public enum EnchantType {
    EFFICIENCY(new Efficiency()),
    CRITICAL(new Critcal()),
    ALCHEMIST(new Alchemist()),
    FORTUNE(new Fortune()),
    COMPACT(new Compact()),
    WISDOM(new Wisdom()),
    EXPLOSIVE(new Explosive()),
    SUPERBREAKER(new SuperBreaker()),
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
