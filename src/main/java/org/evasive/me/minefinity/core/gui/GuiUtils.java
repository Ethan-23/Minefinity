package org.evasive.me.minefinity.core.gui;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;

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

}
