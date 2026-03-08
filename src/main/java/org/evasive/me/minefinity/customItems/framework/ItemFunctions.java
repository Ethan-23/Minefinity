package org.evasive.me.minefinity.customItems.framework;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.utils.TextConversions;

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

    public static BaseCustomItem getRegisteredBaseItem(ItemStack itemStack){
        if(!hasItemId(itemStack))
            return null;
        String itemId = getItemId(itemStack);
        if(!CustomItemRegistry.isRegistered(itemId))
            return null;
        return CustomItemRegistry.getByID(itemId).getBaseItem();
    }

    public static ItemStack getRegisteredItemStack(String itemId){

        ItemBuilder itemBuilder = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, TextConversions.parse(itemId == null ? "<red>Null" :  itemId));
        itemBuilder.addLore("<yellow>This item does not exist YAY!");
        itemBuilder.addLore("<yellow>Make a bug report please!");

        if(!CustomItemRegistry.isRegistered(itemId))
            return itemBuilder.build();

        return CustomItemRegistry.getByID(itemId).getBaseItem().buildItem();
    }

    public static String getStringPDC(ItemStack itemStack, NamespacedKey namespacedKey){
        return itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
    }

}
