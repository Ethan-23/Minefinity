package org.evasive.me.minevolutionCore.customItems.forge;

import org.evasive.me.minevolutionCore.customItems.ItemBuilder;
import org.evasive.me.minevolutionCore.customItems.forge.items.CopperCore;
import org.evasive.me.minevolutionCore.customItems.forge.items.StoneCore;
import org.evasive.me.minevolutionCore.customItems.forge.items.WoodenCore;

public enum ForgeItems {
    WOODEN_CORE(new WoodenCore()),
    STONE_CORE(new StoneCore()),
    COPPER_CORE(new CopperCore());


    private final Object builder;

    // Constructor for BlockType enum
    ForgeItems(Object builder) {
        this.builder = builder;
    }

    public ItemBuilder getItemBuilder() {
        return (ItemBuilder) builder;
    }

    public ForgeItemBuilder getForgeItemBuilder(){
        return (ForgeItemBuilder) builder;
    }
}
