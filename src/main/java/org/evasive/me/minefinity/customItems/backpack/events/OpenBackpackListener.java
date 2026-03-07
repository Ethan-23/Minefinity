package org.evasive.me.minefinity.customItems.backpack.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.backpack.gui.GenericBackpackGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.hasItemId;

public class OpenBackpackListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Action action = event.getAction();

        if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if(!hasItemId(item)) return;

        String itemId = getItemId(item);

        if(!CustomItemRegistry.isRegistered(itemId))return;

       if(CustomItemRegistry.getByID(itemId).getBaseItem().getCustomItemType() != CustomItemType.STORAGE)
           return;

        Player player = event.getPlayer();

        new GenericBackpackGui(player, itemId).open();
    }

}
