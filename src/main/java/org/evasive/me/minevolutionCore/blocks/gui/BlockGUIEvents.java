package org.evasive.me.minevolutionCore.blocks.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.player.PlayerManager;

public class BlockGUIEvents implements Listener {

    final PlayerManager playerManager = MinevolutionCore.getPlayerManager();
    final BlockGUI blockGUI = new BlockGUI();

    @EventHandler
    public void blockGUIClick(InventoryClickEvent e){
        if(!e.getView().title().equals(Component.text("Blocks")))
            return;

        if(e.getClickedInventory() == null)
            return;

        if(e.getClickedInventory().equals(e.getView().getBottomInventory()))
            return;

        int clickedBlock = e.getSlot() + 1;
        Player player = (Player) e.getWhoClicked();

        if(clickedBlock > playerManager.getBlockTier(player)){{
            e.setCancelled(true);
            return;
        }}

        e.setCancelled(true);
        playerManager.setSelectedBlockTier(player, clickedBlock);
        blockGUI.openInventory(player);
    }

}
