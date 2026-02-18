package org.evasive.me.minefinity.player.sevices;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.player.PlayerManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BackpackService {

    PlayerManager playerManager;

    public BackpackService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public int getBackpackStoredItemAmount(Player player, String itemId) {
        Map<String, Integer> backpackMap = playerManager.get(player).getBackpackStorage();

        if(!backpackMap.containsKey(itemId))
            backpackMap.put(itemId, 0);
        return backpackMap.get(itemId);
    }

    public void addBackpackItem(Player player, String itemId, int amount) {
        playerManager.get(player).getBackpackStorage().put(itemId, getBackpackStoredItemAmount(player, itemId) + amount);
    }

    public void removeBackpackItem(Player player, String itemId, int amount) {
        playerManager.get(player).getBackpackStorage().put(itemId, getBackpackStoredItemAmount(player, itemId) - amount);
    }

    public Map<String, Integer> getBackpackStorage(Player player) {
        return Collections.unmodifiableMap(playerManager.get(player).getBackpackStorage());
    }


    public void clearBackpackStorage(Player player) {
        playerManager.get(player).setBackpackStorage(new HashMap<>());
    }
}
