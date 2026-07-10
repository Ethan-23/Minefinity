package org.evasive.me.minefinity.customItems.backpack.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.backpack.gui.GenericBackpackGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.BaseBackpackItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

public class OpenBackpackListener implements Listener {

    private final CustomItemRegistryService customItemRegistryService;
    private final BackpackService backpackService;

    public OpenBackpackListener(CustomItemRegistryService customItemRegistryService, BackpackService backpackService) {
        this.customItemRegistryService = customItemRegistryService;
        this.backpackService = backpackService;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Action action = event.getAction();

        if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.isEmpty()) return;

        if(!customItemRegistryService.isRegistered(item)) return;

        String itemId = customItemRegistryService.getItemId(item);

       if(!(customItemRegistryService.getBaseItemById(itemId) instanceof BaseBackpackItem))
           return;

        Player player = event.getPlayer();

        new GenericBackpackGui(player, itemId, customItemRegistryService, backpackService).open();
    }

}
