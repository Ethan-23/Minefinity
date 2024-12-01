package org.evasive.me.minevolutionCore.customItems;

import org.evasive.me.minevolutionCore.customItems.items.components.CopperCore;
import org.evasive.me.minevolutionCore.customItems.items.components.StoneCore;
import org.evasive.me.minevolutionCore.customItems.items.components.WoodenCore;
import org.evasive.me.minevolutionCore.customItems.items.storage.Backpack;

public enum ItemList {
    WOODEN_CORE(new WoodenCore()),
    STONE_CORE(new StoneCore()),
    COPPER_CORE(new CopperCore()),
    BACKPACK(new Backpack()),
    BROKEN_PICKAXE(PickaxeItems.BROKEN_PICKAXE.getPickaxeBuilder()),
    WOODEN_PICKAXE(PickaxeItems.WOODEN_PICKAXE.getPickaxeBuilder()),
    STONE_PICKAXE(PickaxeItems.STONE_PICKAXE.getPickaxeBuilder()),
    COPPER_PICKAXE(PickaxeItems.COPPER_PICKAXE.getPickaxeBuilder())
    ;

    private final ItemBuilder builder;

    ItemList(ItemBuilder itemBuilder) {
        this.builder = itemBuilder;
    }

    public ItemBuilder getBuilder(){
        return builder;
    }
}
