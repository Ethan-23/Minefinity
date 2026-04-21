package org.evasive.me.minefinity.customItems.backpack.events;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCollectItem;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.Map;

public class ItemPickupListener implements Listener {

    private final CustomItemRegistryService customItemRegistryService;
    private final ItemPickupService itemPickupService;

    public ItemPickupListener(CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService) {
        this.customItemRegistryService = customItemRegistryService;
        this.itemPickupService = itemPickupService;
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent e) {

        Player player = e.getPlayer();
        Item itemEntity = e.getItem();
        ItemStack stack = itemEntity.getItemStack();

        e.setCancelled(true);

        int remaining = stack.getAmount();
        int original = remaining;

        // Non-custom item
        if (!customItemRegistryService.isRegistered(stack)){
            Map<Integer, ItemStack> leftover = player.getInventory().addItem(stack);

            int added = original - leftover.values().stream().mapToInt(ItemStack::getAmount).sum();

            if (added == 0)
                return;

            playPickup(player, itemEntity, added);

            if(added == original)
                itemEntity.remove();
            else
                itemEntity.getItemStack().setAmount(original - added);

            return;
        }

        itemEntity.setItemStack(customItemRegistryService.rebuildItem(stack));
        stack = itemEntity.getItemStack();


        // Backpack
        int beforeBackpack = remaining;

        remaining = itemPickupService.attemptBackpackStorage(player, stack, remaining); // returns remaining

        if (remaining < beforeBackpack) {
            playPickup(player, itemEntity, beforeBackpack - remaining);
        }

        if (remaining <= 0) {
            itemEntity.remove();
            return;
        }

        //Inventory
        int beforeInventory = remaining;

        remaining = itemPickupService.attemptInventoryStorage(player, stack, remaining);

        if (remaining < beforeInventory) {
            playPickup(player, itemEntity, beforeInventory - remaining);
        }

        if (remaining > 0) {
            itemPickupService.fullInventoryNotification(player);

            ItemStack newStack = stack.clone();
            newStack.setAmount(remaining);

            itemEntity.setItemStack(newStack);
        }else {
            itemEntity.remove();
        }
    }

    public void playPickup(Player player, Item itemEntity, int amount) {
        WrapperPlayServerCollectItem packet =
                new WrapperPlayServerCollectItem(
                        itemEntity.getEntityId(),
                        player.getEntityId(),
                        amount
                );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);

        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 2f);
    }
}
