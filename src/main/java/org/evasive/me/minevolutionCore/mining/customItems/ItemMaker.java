package org.evasive.me.minevolutionCore.mining.customItems;

import org.bukkit.inventory.ItemStack;

import org.evasive.me.minevolutionCore.mining.customItems.forge.ForgeItems;
import org.evasive.me.minevolutionCore.mining.customItems.pickaxes.PickaxeItems;


public class ItemMaker {
    public static ItemStack woodenCore;
    public static ItemStack brokenPickaxe;
    public static ItemStack woodenPickaxe;
    public static ItemStack stonePickaxe;
    public static ItemStack copperPickaxe;

    public void init(){
        woodenCore = ForgeItems.WOODEN_CORE.getItemBuilder().buildItem();
        brokenPickaxe = PickaxeItems.BROKEN_PICKAXE.getPickaxeBuilder().buildItem(1, 0);
        woodenPickaxe = PickaxeItems.WOODEN_PICKAXE.getPickaxeBuilder().buildItem(1, 0);
        stonePickaxe = PickaxeItems.STONE_PICKAXE.getPickaxeBuilder().buildItem(1, 0);
        copperPickaxe = PickaxeItems.COPPER_PICKAXE.getPickaxeBuilder().buildItem(1, 0);
    }
}
