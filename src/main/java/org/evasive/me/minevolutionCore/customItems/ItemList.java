package org.evasive.me.minevolutionCore.customItems;

import org.evasive.me.minevolutionCore.customItems.forge.ForgeItems;
import org.evasive.me.minevolutionCore.customItems.pickaxes.PickaxeItems;

public enum ItemList {
    WOODEN_CORE(ForgeItems.WOODEN_CORE.getItemBuilder()),
    STONE_CORE(ForgeItems.STONE_CORE.getItemBuilder()),
    COPPER_CORE(ForgeItems.COPPER_CORE.getItemBuilder()),
    BROKEN_PICKAXE(PickaxeItems.BROKEN_PICKAXE.getPickaxeBuilder()),
    WOODEN_PICKAXE(PickaxeItems.WOODEN_PICKAXE.getPickaxeBuilder()),
    STONE_PICKAXE(PickaxeItems.STONE_PICKAXE.getPickaxeBuilder()),
    COPPER_PICKAXE(PickaxeItems.COPPER_PICKAXE.getPickaxeBuilder())
    ;

    private ItemBuilder builder;

    ItemList(ItemBuilder itemBuilder) {
        this.builder = itemBuilder;
    }

    public ItemBuilder getBuilder(){
        return builder;
    }
}
