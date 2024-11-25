package org.evasive.me.minevolutionCore.customItems;

import org.bukkit.inventory.ItemStack;

import org.evasive.me.minevolutionCore.customItems.forge.ForgeItems;
import org.evasive.me.minevolutionCore.customItems.pickaxes.PickaxeItems;


public class ItemMaker {
    public static ItemStack woodenCore;
    public static ItemStack stoneCore;
    public static ItemStack copperCore;
    public static ItemStack brokenPickaxe;
    public static ItemStack woodenPickaxe;
    public static ItemStack stonePickaxe;
    public static ItemStack copperPickaxe;

    public void init(){
        woodenCore = ForgeItems.WOODEN_CORE.getItemBuilder().buildItem();
        stoneCore = ForgeItems.STONE_CORE.getForgeItemBuilder().buildItem();
        copperCore = ForgeItems.COPPER_CORE.getForgeItemBuilder().buildItem();
        brokenPickaxe = PickaxeItems.BROKEN_PICKAXE.getPickaxeBuilder().buildItem(1, 0, null);
        woodenPickaxe = PickaxeItems.WOODEN_PICKAXE.getPickaxeBuilder().buildItem(1, 0, null);
        stonePickaxe = PickaxeItems.STONE_PICKAXE.getPickaxeBuilder().buildItem(1, 0, null);
        copperPickaxe = PickaxeItems.COPPER_PICKAXE.getPickaxeBuilder().buildItem(1, 0, null);
    }
}
