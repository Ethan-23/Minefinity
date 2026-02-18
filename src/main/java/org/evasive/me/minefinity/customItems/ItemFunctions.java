package org.evasive.me.minefinity.customItems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.backpack.Backpacks;
import org.evasive.me.minefinity.customItems.backpack.BaseBackpackItem;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeComponent;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;

import static org.evasive.me.minefinity.core.items.BaseCustomItem.itemIDKey;

public class ItemFunctions {


    public static String getItemId(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().get(itemIDKey, PersistentDataType.STRING);
    }

    public static boolean hasItemId(ItemStack itemStack){
        if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().getPersistentDataContainer().has(itemIDKey)) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().has(itemIDKey);
    }

    public static boolean isPickaxe(ItemStack itemStack){
        if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().getPersistentDataContainer().has(itemIDKey)) return false;
        return PickaxeItem.contains(getItemId(itemStack));
    }

    public static String getStringPDC(ItemStack itemStack, NamespacedKey namespacedKey){
        return itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
    }

    /**
     * Gets the pickaxe item from a pickaxe item stack
     * @param itemStack must be an itemstack with the pickaxe item ID
     * @return BasePickaxeItem of the itemstack
     */
    public static BasePickaxeItem getPickaxeItem(ItemStack itemStack){
        return PickaxeItem.valueOf(getStringPDC(itemStack, itemIDKey)).getBuilder();
    }

    /**
     * Gets the pickaxe component from a pickaxe item stack
     * @param itemStack must be an itemstack with the component item ID
     * @return BasePickaxeComponent of the itemstack
     */
    public static BasePickaxeComponent getPickaxeComponent(ItemStack itemStack){
        return PickaxeComponent.valueOf(getStringPDC(itemStack, itemIDKey)).getBuilder();
    }

    public static BaseBackpackItem getBackpackItem(ItemStack itemStack){
        return Backpacks.valueOf(getStringPDC(itemStack, itemIDKey)).getBuilder();
    }

}
