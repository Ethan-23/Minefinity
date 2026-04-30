package org.evasive.me.minefinity.towns.structures.forge.smelter.service;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.Map;

public class SmelterHandler {

    SmelterService smelterService;
    private final ItemPickupService itemPickupService;
    private final CustomItemRegistryService customItemRegistryService;

    public SmelterHandler(SmelterService smelterService, CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService) {
        this.smelterService = smelterService;
        this.itemPickupService = itemPickupService;
        this.customItemRegistryService = customItemRegistryService;
    }

    public void handleCollectSlot(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        Map<String, Integer> itemMap = smelterService.getOutput(player);

        for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {

            int remaining = entry.getValue();

            ItemStack drop = customItemRegistryService.getRegisteredItemStack(entry.getKey());

            int overflow = itemPickupService.givePlayerDrops(player, drop, remaining);

            smelterService.removeSmelterStorage(player, entry.getKey(), remaining - overflow);
        }
    }


}
