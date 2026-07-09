package org.evasive.me.minefinity.core.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.lib.item.ItemBuilder;

import java.util.List;

public class GuiUtils {

    public static void fillGui(Inventory inventory) {
        for(int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack blank = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "").build();
            inventory.setItem(slot, blank);
        }
    }

    public static void fillGui(Inventory inventory, List<Integer> ignoreSlots) {
        for(int slot = 0; slot < inventory.getSize(); slot++) {

            if(ignoreSlots.contains(slot))
                continue;
            ItemStack blank = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "").build();
            inventory.setItem(slot, blank);
        }
    }

    /**
     * Puts {@code item} (or AIR when null) on the player's cursor, sized to {@code amount}.
     * A shift-left click instead drops it into the first free bottom-inventory slot.
     */
    public static void swapCursor(InventoryClickEvent e, ItemStack item, int amount) {
        ItemStack returnItem = new ItemStack(Material.AIR);
        if (item != null) {
            returnItem = item;
            returnItem.setAmount(amount);
        }
        if (e.getClick() == ClickType.SHIFT_LEFT && e.getView().getBottomInventory().firstEmpty() != -1) {
            e.getView().getBottomInventory().addItem(returnItem);
        } else {
            e.getView().setCursor(returnItem);
        }
    }

}
