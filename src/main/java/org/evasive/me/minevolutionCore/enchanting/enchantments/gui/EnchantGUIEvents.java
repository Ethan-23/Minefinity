package org.evasive.me.minevolutionCore.enchanting.enchantments.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EnchantGUIEvents implements Listener {

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e){
        if(e.getView().title().equals(Component.text("Enchant List"))){
            e.setCancelled(true);
        }
    }
}
