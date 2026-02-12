package org.evasive.me.minefinity.core.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getView().getTopInventory().getHolder();
        if (!(holder instanceof BaseGui gui)) return;

        if(e.getClickedInventory() == null)
            return;

        if (e.isShiftClick()) {
            e.setCancelled(true);
        }

        if(e.getClick() == ClickType.DOUBLE_CLICK) {
            e.setCancelled(true);
            return;
        }

        if(e.getView().getTopInventory() != e.getClickedInventory())
            return;

        if (e.getHotbarButton() != -1) {
            e.setCancelled(true);
            return;
        }

        gui.onClick(e);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        InventoryHolder holder = e.getView().getTopInventory().getHolder();
        if (holder instanceof BaseGui gui) {
            e.setCancelled(true);
            gui.onDrag(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof BaseGui gui) {
            gui.onClose(e);
        }
    }

}
