package org.evasive.me.minevolutionCore.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.mining.BlockProgress;
import org.evasive.me.minevolutionCore.mining.SwingPacketEvents;
import org.evasive.me.minevolutionCore.worldPackets.MiningBlockHandler;

import java.util.*;

import static org.evasive.me.minevolutionCore.MinevolutionCore.*;

public class PlayerManager {
    private final HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();
    Map<UUID, Material> playerMaterialSelection = new HashMap<>();
    
    public void registerPlayer(Player player) {
        PlayerData playerData = new PlayerData(player);
        playerDataMap.put(player.getUniqueId(), playerData);

    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public Map<UUID, PlayerData> getAllPlayersData() {
        return Collections.unmodifiableMap(playerDataMap);
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
        Block block = selectedBlockMap.getSelectedBlock(player.getUniqueId());
        if(block == null || !miningMap.containsBlockLocation(block.getLocation()) || !miningMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))return;
        new BlockProgress().sendCleanPacket(player, block);
        miningMap.removeBlockPos(block.getLocation(), player.getUniqueId());
    }

    public void setMaterialSelection(Player player, Material material){
        playerMaterialSelection.put(player.getUniqueId(), material);
    }

    public Material getMaterialSelection(Player player){
        return playerMaterialSelection.get(player.getUniqueId());
    }

    public void removeMaterialSelection(Player player){
        playerMaterialSelection.remove(player.getUniqueId());
    }

    public int getBackpackStoredItemAmount(Player player, String itemId) {
        PlayerData playerData = getPlayerData(player);
        if(!playerData.getBackpackStorage().containsKey(itemId))
            playerData.getBackpackStorage().put(itemId, 0);
        return playerData.getBackpackStorage().get(itemId);
    }

    public void addBackpackItem(Player player, String itemId, int amount) {
        PlayerData playerData = getPlayerData(player);
        playerData.getBackpackStorage().put(itemId, getBackpackStoredItemAmount(player, itemId) + amount);
    }

    public void removeBackpackItem(Player player, String itemId, int amount) {
        PlayerData playerData = getPlayerData(player);
        playerData.getBackpackStorage().put(itemId, getBackpackStoredItemAmount(player, itemId) - amount);
    }

    public Map<String, Integer> getBackpackStorage(Player player) {
        PlayerData playerData = getPlayerData(player);
        return Collections.unmodifiableMap(playerData.getBackpackStorage());
    }

    public void clearBackpackStorage(Player player) {
        PlayerData playerData = getPlayerData(player);
        playerData.clearBackpack();
    }

}
