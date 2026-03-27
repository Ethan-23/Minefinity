package org.evasive.me.minefinity.customItems.backpack;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;

import java.util.Map;

public class BackpackService {

    private final PlayerDataService playerDataService;

    public BackpackService(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    private PlayerData getPlayerData(Player player) {
        return playerDataService.getPlayerData(player.getUniqueId());
    }

    public Map<String, Integer> getBackpackStorage(Player player){
        return getPlayerData(player).getBackpackStorage();
    }

    public int getBackpackStoredItemAmount(Player player, String itemId) {
        if(!getPlayerData(player).getBackpackStorage().containsKey(itemId))
            setBackpackItem(player, itemId, 0);
        return getBackpackStorage(player).get(itemId);
    }

    private void setBackpackItem(Player player, String itemId, int amount) {
        getPlayerData(player).changeBackpackStorage(itemId, amount);
    }

    public void removeBackpackItem(Player player, String itemId, int amount) {
        setBackpackItem(player, itemId, -amount);
    }

    public void addBackpackItem(Player player, String itemId, int amount) {
        setBackpackItem(player, itemId, amount);
    }

    public void clearBackpackStorage(Player player) {
        getPlayerData(player).clearBackpackStorage();
    }
}
