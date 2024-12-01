package org.evasive.me.minevolutionCore.customItems;

import org.evasive.me.minevolutionCore.customItems.items.pickaxes.BrokenPickaxe;
import org.evasive.me.minevolutionCore.customItems.items.pickaxes.CopperPickaxe;
import org.evasive.me.minevolutionCore.customItems.items.pickaxes.StonePickaxe;
import org.evasive.me.minevolutionCore.customItems.items.pickaxes.WoodenPickaxe;

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
