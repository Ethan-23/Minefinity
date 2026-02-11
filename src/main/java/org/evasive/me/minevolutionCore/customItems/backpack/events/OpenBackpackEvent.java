package org.evasive.me.minevolutionCore.customItems.backpack.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.backpack.Backpacks;
import org.evasive.me.minevolutionCore.customItems.backpack.gui.GenericBackpackGui;

import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.getItemId;
import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.hasItemId;

public class OpenBackpackEvent implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Action action = event.getAction();

        if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if(!hasItemId(item)) return;

        String itemId = getItemId(item);

        if(!Backpacks.contains(itemId))return;

        Player player = event.getPlayer();

        new GenericBackpackGui(player, itemId).open();
    }

}
