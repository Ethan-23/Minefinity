package org.evasive.me.minefinity.core.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryHolder;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getView().getTopInventory().getHolder();

        if (!(holder instanceof BaseGui gui)) return;

        if(e.getClickedInventory() == null)
            return;

        if(e.getClick() == ClickType.DOUBLE_CLICK) {
            e.setCancelled(true);
            return;
        }

        if(e.getClickedInventory() == e.getView().getBottomInventory() && e.getClick().isShiftClick())
            e.setCancelled(true);

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

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof BaseGui gui) {
            gui.onOpen(e);
        }
    }

}
