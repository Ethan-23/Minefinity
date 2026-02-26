package org.evasive.me.minefinity.smelter.service;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.Minefinity;

import java.util.Map;

public class SmelterHandler {

    SmelterService smelterService = Minefinity.core.getSmelterService();

    public void handleCollectSlot(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        Map<String, Integer> itemMap = smelterService.getOutput(player);

        for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
            int remaining = entry.getValue();

            int overflow = Minefinity.itemGiver.givePlayerDrops(player, entry.getKey(), remaining);

            smelterService.removeSmelterStorage(player, entry.getKey(), remaining - overflow);

        }
    }

}
