package org.evasive.me.minevolutionCore.player;

import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.worldPackets.MiningBlockHandler;

import java.util.*;

public class PlayerManager {
    private final HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();



    public void registerPlayer(Player player) {
        PlayerData playerData = new PlayerData(player);
        playerDataMap.put(player.getUniqueId(), playerData);

    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public boolean hasPlayerData(Player player){
        return playerDataMap.containsKey(player.getUniqueId());
    }

    public void setBlockTier(Player player, int amount){
        playerDataMap.get(player.getUniqueId()).setBlockTier(amount);
        /*new MiningBlockHandler().replaceBlockPacketsInRegion(player);*/
    }

    public void addBlocksMined(Player player, int amount){
        UUID uuid = player.getUniqueId();
        playerDataMap.get(uuid).setBlocksMined(playerDataMap.get(uuid).getBlocksMined() + amount);
    }

    public int getBlocksMined(Player player){
        return getPlayerData(player).getBlocksMined();
    }

    public int getBlockTier(Player player){
        return getPlayerData(player).getBlockTier();
    }

    public int getQuestLevel(Player player){
        return getPlayerData(player).getQuest();
    }

    public int getSelectedBlockTier(Player player){
        return getPlayerData(player).getSelectedBlockTier();
    }

    public void setSelectedBlockTier(Player player, int tier){
        getPlayerData(player).setSelectedBlockTier(tier);
        new MiningBlockHandler().replaceBlockPacketsInRegion(player, player.getWorld());
    }

}
