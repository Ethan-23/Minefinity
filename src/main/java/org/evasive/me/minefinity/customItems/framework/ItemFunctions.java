package org.evasive.me.minefinity.customItems.framework;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class ItemFunctions {


    public static String getItemId(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().get(ITEM_ID_KEY, PersistentDataType.STRING);
    }

    public static String getItemType(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().get(ITEM_TYPE_KEY, PersistentDataType.STRING);
    }

    public static boolean hasItemId(ItemStack itemStack){
        if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().getPersistentDataContainer().has(ITEM_ID_KEY)) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().has(ITEM_ID_KEY);
    }

    public static BaseCustomItem getRegisteredItem(ItemStack itemStack){
        if(!hasItemId(itemStack))
            return null;
        String itemId = getItemId(itemStack);
        if(!CustomItemRegistry.isRegistered(itemId))
            return null;
        return CustomItemRegistry.getByID(itemId).getBaseItem();
    }

    public static String getStringPDC(ItemStack itemStack, NamespacedKey namespacedKey){
        return itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
    }

}
