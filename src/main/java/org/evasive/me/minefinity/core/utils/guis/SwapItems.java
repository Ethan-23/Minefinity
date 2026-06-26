package org.evasive.me.minefinity.core.utils.guis;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;

public class SwapItems
{
    public static void swapCursor(InventoryClickEvent e, BaseCustomItem customItem, int amount){

        ItemStack returnItem = new ItemStack(Material.AIR);

        if(customItem != null){
            returnItem = new ItemStack(customItem.getBaseItem().buildItem());
            returnItem.setAmount(amount);
        }
        if(e.getClick() == ClickType.SHIFT_LEFT && e.getView().getBottomInventory().firstEmpty() != -1){
            e.getView().getBottomInventory().addItem(returnItem);
        }else {
            e.getView().setCursor(returnItem);
        }

    }
}
