package org.evasive.me.minevolutionCore.mining.customItems.pickaxes;

import org.evasive.me.minevolutionCore.mining.customItems.pickaxes.items.BrokenPickaxe;
import org.evasive.me.minevolutionCore.mining.customItems.pickaxes.items.CopperPickaxe;
import org.evasive.me.minevolutionCore.mining.customItems.pickaxes.items.StonePickaxe;
import org.evasive.me.minevolutionCore.mining.customItems.pickaxes.items.WoodenPickaxe;

public enum PickaxeItems {
    BROKEN_PICKAXE(new BrokenPickaxe()),
    WOODEN_PICKAXE(new WoodenPickaxe()),
    STONE_PICKAXE(new StonePickaxe()),
    COPPER_PICKAXE(new CopperPickaxe());

    private final PickaxeItemBuilder pickaxeBuilder;

    // Constructor for BlockType enum
    PickaxeItems(PickaxeItemBuilder blockCreator) {
        this.pickaxeBuilder = blockCreator;
    }

    public PickaxeItemBuilder getPickaxeBuilder() {
        return pickaxeBuilder;
    }
}
