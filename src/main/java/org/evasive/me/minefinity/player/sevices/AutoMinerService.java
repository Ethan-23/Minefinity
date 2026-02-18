package org.evasive.me.minefinity.player.sevices;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.automation.miner.data.AutoMiner;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.resourceblock.BlockType;

import java.util.Map;

public class AutoMinerService {

    private final PlayerManager playerManager;

    public AutoMinerService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public AutoMiner getAutoMiner(Player player) {
        return playerManager.get(player).getAutoMiner();
    }

    public void setAutoMinerBlockType(Player player, BlockType blockType) {
        getAutoMiner(player).setBlockType(blockType);
    }

    public BlockType getAutoMinerBlockType(Player player) {
        return getAutoMiner(player).getBlockType();
    }

    public void setAutoMinerPickaxe(Player player, ItemStack itemStack) {
        getAutoMiner(player).setPickaxe(itemStack);
    }

    public ItemStack getAutoMinerPickaxe(Player player) {
        return getAutoMiner(player).getPickaxe();
    }

    public void setAutoMinerLevel(Player player, int level) {
        getAutoMiner(player).setLevel(level);
    }

    public int getAutoMinerLevel(Player player) {
        return getAutoMiner(player).getLevel();
    }

    public int getAutoMinerStorageCap(Player player){
        return getAutoMiner(player).getStorageCap();
    }

    public void setAutoMinerStorageCap(Player player, int amount){
        getAutoMiner(player).setStorageCap(amount);
    }

    public int getAutoMinerStoredBlockAmount(Player player) {
        return getAutoMiner(player).getStoredBlockAmount();
    }

    public Map<String, Integer> getAutoMinerStorage(Player player){
        return getAutoMiner(player).getItemStorage();
    }

    public void removeAutoMinerStorage(Player player, String itemId, int amount){
        getAutoMiner(player).removeItemStorage(itemId, amount);
    }


}
