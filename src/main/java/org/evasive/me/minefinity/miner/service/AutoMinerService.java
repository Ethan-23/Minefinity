package org.evasive.me.minefinity.miner.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.miner.AutoMiner;
import org.evasive.me.minefinity.mining.service.BlockProgress;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.Map;

public class AutoMinerService {

    private final PlayerManager playerManager;
    private final DirtyPlayerService dirtyPlayerService = Minefinity.getCore().getDirtyPlayerService();

    public AutoMinerService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    private AutoMiner getAutoMiner(Player player) {
        return playerManager.get(player).getAutoMiner();
    }

    public void setAutoMinerBlockType(Player player, BlockType blockType) {
        dirtyPlayerService.addDirtyPlayer(player);
        getAutoMiner(player).setBlockType(blockType);
    }

    public BlockType getAutoMinerBlockType(Player player) {
        return getAutoMiner(player).getBlockType();
    }

    public void setAutoMinerPickaxe(Player player, ItemStack itemStack) {
        dirtyPlayerService.addDirtyPlayer(player);
        getAutoMiner(player).setPickaxe(itemStack == null ? null : new BasePickaxeItem(itemStack));
    }

    public ItemStack getAutoMinerPickaxe(Player player) {
        BasePickaxeItem pickaxeItem = getAutoMiner(player).getPickaxe();
        return pickaxeItem == null ? null : pickaxeItem.buildItem();
    }

    public void setAutoMinerLevel(Player player, int level) {
        dirtyPlayerService.addDirtyPlayer(player);
        getAutoMiner(player).setLevel(level);
    }

    public int getAutoMinerLevel(Player player) {
        return getAutoMiner(player).getLevel();
    }

    public int getAutoMinerStorageCap(Player player){
        return getAutoMiner(player).getStorageCap();
    }

    public void setAutoMinerStorageCap(Player player, int amount){
        dirtyPlayerService.addDirtyPlayer(player);
        getAutoMiner(player).setStorageCap(amount);
    }

    public int getAutoMinerStoredBlockAmount(Player player) {
        return getAutoMiner(player).getStoredBlockAmount();
    }

    public Map<String, Integer> getAutoMinerStorage(Player player){
        return getAutoMiner(player).getItemStorage();
    }

    public void removeAutoMinerStorage(Player player, String itemId, int amount){
        dirtyPlayerService.addDirtyPlayer(player);
        getAutoMiner(player).removeItemStorage(itemId, amount);
    }

    public long getLastUpdated(Player player){
        return getAutoMiner(player).getLastUpdated();
    }

    public void setLastUpdated(Player player){
        dirtyPlayerService.addDirtyPlayer(player);
        getAutoMiner(player).setLastUpdated(System.currentTimeMillis());
    }

    public void addProgress(Player player, long secondsPassed){
        AutoMiner autoMiner = getAutoMiner(player);

        BlockType blockType = getAutoMinerBlockType(player);

        float blockHardness = blockType.getBlock().health();
        float progressPerTick = autoMiner.getPickaxe().getBaseMiningSpeed();

        if(progressPerTick > blockHardness / BlockProgress.MAX_SPEED_DENOMINATION)
            progressPerTick = blockHardness / BlockProgress.MAX_SPEED_DENOMINATION;

        float storedProgress = autoMiner.getStoredProgress();

        autoMiner.setStoredProgress(storedProgress + (progressPerTick * 20) * secondsPassed);

        int blocksMined = (int) (storedProgress / blockHardness);

        if (blocksMined > 0) {
            autoMiner.setStoredProgress(storedProgress - blocksMined * blockHardness);
            autoMiner.addItemStorage(blockType.getBlock().blockDrop().getID(), blocksMined);
        }

        setLastUpdated(player);
    }

    public boolean isStorageFull(Player player) {
        return getAutoMiner(player).isFull();
    }

    public void addOfflineProgress(Player player){
        if(getAutoMinerBlockType(player) == null || getAutoMinerPickaxe(player) == null || isStorageFull(player)) return;
        int offlineTimeCap = getAutoMiner(player).getOfflineProgress();
        long secondsSinceLastUpdate = Math.min(offlineTimeCap * 60L, System.currentTimeMillis()/1000 - getLastUpdated(player)/1000);
        addProgress(player, secondsSinceLastUpdate);
        player.sendMessage(TextConversions.parse("<gold>Miner has made progress while you were offline!"));
    }

    public void attemptAutomine(Player player, int progressTime){
        if(getAutoMinerBlockType(player) == null || getAutoMinerPickaxe(player) == null || isStorageFull(player)) return;
        addProgress(player, progressTime);
    }
}
