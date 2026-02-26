package org.evasive.me.minefinity.customItems.backpack;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.player.PlayerManager;

import java.util.Map;

public class BackpackService {

    PlayerManager playerManager;
    DirtyPlayerService dirtyPlayerService = Minefinity.getCore().getDirtyPlayerService();

    public BackpackService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    private BackpackStorage getBackpackStorage(Player player){
        return playerManager.get(player).getBackpackStorage();
    }

    public int getBackpackStoredItemAmount(Player player, String itemId) {
        return getBackpackStorage(player).getResourceCount(itemId);
    }

    public void addBackpackItem(Player player, String itemId, int amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        getBackpackStorage(player).setResourceCount(itemId, getBackpackStoredItemAmount(player, itemId) + amount);
    }

    public void removeBackpackItem(Player player, String itemId, int amount) {
        getBackpackStorage(player).setResourceCount(itemId, getBackpackStoredItemAmount(player, itemId) - amount);
    }

    public Map<String, Integer> getBackpackItems(Player player) {
        dirtyPlayerService.addDirtyPlayer(player);
        return getBackpackStorage(player).getBackpackMap();
    }

    public void clearBackpackStorage(Player player) {
        dirtyPlayerService.addDirtyPlayer(player);
        getBackpackStorage(player).clearBackpack();
    }
}
