package org.evasive.me.minevolutionCore.mining.customItems.forge;

import org.evasive.me.minevolutionCore.mining.customItems.ItemBuilder;
import org.evasive.me.minevolutionCore.mining.customItems.forge.items.WoodenCore;

public enum ForgeItems {
    WOODEN_CORE(new WoodenCore());


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
