package org.evasive.me.minevolutionCore.mining.customItems;

import org.evasive.me.minevolutionCore.mining.customItems.forge.ForgeItems;
import org.evasive.me.minevolutionCore.mining.customItems.pickaxes.PickaxeItems;

public enum ItemList {
    WOOD_CORE(ForgeItems.WOODEN_CORE.getItemBuilder()),
    BROKEN_PICKAXE(PickaxeItems.BROKEN_PICKAXE.getPickaxeBuilder()),
    WOODEN_PICKAXE(PickaxeItems.WOODEN_PICKAXE.getPickaxeBuilder()),
    STONE_PICKAXE(PickaxeItems.STONE_PICKAXE.getPickaxeBuilder()),
    COPPER_PICKAXE(PickaxeItems.COPPER_PICKAXE.getPickaxeBuilder())
    ;
//    BROKEN_PICKAXE(PickaxeType.BROKEN_PICKAXE),
//    WOODEN_PICKAXE(PickaxeType.WOODEN_PICKAXE),
//    STONE_PICKAXE(PickaxeType.STONE_PICKAXE),
//    COPPER_PICKAXE(PickaxeType.COPPER_PICKAXE);

    private ItemBuilder builder;

    ItemList(ItemBuilder itemBuilder) {
        this.builder = itemBuilder;
    }

    public ItemBuilder getBuilder(){
        return builder;
    }
}
