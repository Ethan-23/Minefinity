package org.evasive.me.minefinity.customItems.backpack;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;

import java.util.Map;

public class BackpackService {

    private final PlayerDataService playerDataService;

    public BackpackService(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    private BackpackData backpack(Player player) {
        return playerDataService.getPlayerData(player).get(BackpackData.class);
    }

    public Map<String, Integer> getBackpackStorage(Player player){
        return backpack(player).getBackpackStorage();
    }

    public int getBackpackStoredItemAmount(Player player, String itemId) {
        if(!backpack(player).getBackpackStorage().containsKey(itemId))
            setBackpackItem(player, itemId, 0);
        return getBackpackStorage(player).get(itemId);
    }

    private void setBackpackItem(Player player, String itemId, int amount) {
        backpack(player).changeBackpackStorage(itemId, amount);
    }

    public void removeBackpackItem(Player player, String itemId, int amount) {
        setBackpackItem(player, itemId, -amount);
    }

    public void addBackpackItem(Player player, String itemId, int amount) {
        setBackpackItem(player, itemId, amount);
    }

    public void clearBackpackStorage(Player player) {
        backpack(player).clearBackpackStorage();
    }
}
