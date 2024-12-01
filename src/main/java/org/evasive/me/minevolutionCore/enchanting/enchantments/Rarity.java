package org.evasive.me.minevolutionCore.enchanting.enchantments;

import org.evasive.me.minevolutionCore.enchanting.enchantments.rarity.*;

public enum Rarity {
    MINOR(new Minor()),
    UNIQUE(new Unique()),
    RADIANT(new Radiant()),
    EXQUISITE(new Exquisite()),
    PRISTINE(new Pristine()),
    MYTHIC(new Mythic());

    private final RarityBuilder rarityBuilder;

    // Constructor for BlockType enum
    Rarity(RarityBuilder rarityBuilder) {
        this.rarityBuilder = rarityBuilder;
    }

    public RarityBuilder getRarityBuilder() {
        return rarityBuilder;
    }

}


