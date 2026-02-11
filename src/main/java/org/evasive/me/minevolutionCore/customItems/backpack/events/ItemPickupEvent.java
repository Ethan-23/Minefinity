package org.evasive.me.minevolutionCore.customItems.backpack.events;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.MinevolutionCore;

import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.getItemId;
import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.hasItemId;

public class ItemPickupEvent implements Listener {
    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent e) {

        ItemStack itemStack = e.getItem().getItemStack();

        if(!hasItemId(itemStack)) return;

        Player player = e.getPlayer();

        int overflow = MinevolutionCore.itemGiver.givePlayerDrops(player, getItemId(itemStack), itemStack.getAmount());

        if(overflow == itemStack.getAmount()) return;

        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 2);

        if(overflow > 0) {
            itemStack.setAmount(overflow);
            return;
        }

        e.getItem().remove();
        e.setCancelled(true);
    }
}
