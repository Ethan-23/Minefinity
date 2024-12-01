package org.evasive.me.minevolutionCore.customItems;

import org.bukkit.inventory.ItemStack;


public class ItemMaker {
    public static ItemStack woodenCore;
    public static ItemStack stoneCore;
    public static ItemStack copperCore;
    public static ItemStack brokenPickaxe;
    public static ItemStack woodenPickaxe;
    public static ItemStack stonePickaxe;
    public static ItemStack copperPickaxe;
    public static ItemStack backpack;

    public void init(){
        woodenCore = ItemList.WOODEN_CORE.getBuilder().buildItem();
        stoneCore = ItemList.STONE_CORE.getBuilder().buildItem();
        copperCore = ItemList.COPPER_CORE.getBuilder().buildItem();
        backpack = ItemList.BACKPACK.getBuilder().buildItem();
        brokenPickaxe = PickaxeItems.BROKEN_PICKAXE.getPickaxeBuilder().buildItem(1, 0, null);
        woodenPickaxe = PickaxeItems.WOODEN_PICKAXE.getPickaxeBuilder().buildItem(1, 0, null);
        stonePickaxe = PickaxeItems.STONE_PICKAXE.getPickaxeBuilder().buildItem(1, 0, null);
        copperPickaxe = PickaxeItems.COPPER_PICKAXE.getPickaxeBuilder().buildItem(1, 0, null);
    }
}
