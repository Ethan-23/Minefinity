package org.evasive.me.minefinity.towns.structures.mines.miner.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StatsComponent;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.utils.MiningProgress;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.towns.structures.mines.miner.AutoMinerData;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;

import java.util.Map;

public class AutoMinerService {

    private final PlayerDataService playerDataService;
    private final CustomItemRegistryService customItemRegistryService;
    private final BlockTypeRegistryService blockTypeRegistryService;

    public AutoMinerService(PlayerDataService playerDataService, CustomItemRegistryService customItemRegistryService, BlockTypeRegistryService blockTypeRegistryService) {
        this.playerDataService = playerDataService;
        this.customItemRegistryService = customItemRegistryService;
        this.blockTypeRegistryService = blockTypeRegistryService;
    }

    private AutoMinerData getAutoMiner(Player player) {
        return playerDataService.getPlayerData(player).get(AutoMinerData.class);
    }

    public int getAutoMinerUnlockedBlockTier(Player player){
        return getAutoMiner(player).getUnlockedBlockTier();
    }

    public void setAutoMinerBlockType(Player player, String blockId) {
        if(blockId == null) return;
        getAutoMiner(player).setSelectedBlock(blockId);
    }

    public String getAutoMinerBlockType(Player player) {
        return getAutoMiner(player).getSelectedBlock();
    }

    public void setAutoMinerPickaxe(Player player, BasePickaxeItem basePickaxeItem) {
        //Need to change to allow parts
        getAutoMiner(player).setPickaxeItemId(basePickaxeItem.getID());
    }

    public boolean hasAutoMinerPickaxe(Player player) {
        return !(getAutoMiner(player).getPickaxeId() == null);
    }

    public ItemStack getAutoMinerPickaxe(Player player) {
        //Need to rework to account for parts
        BasePickaxeItem pickaxeItem = (BasePickaxeItem) customItemRegistryService.getBaseItemById(getAutoMiner(player).getPickaxeId());
        return pickaxeItem == null ? null : pickaxeItem.buildItem();
    }

    public int getAutoMinerStorageCap(Player player){
        return getAutoMiner(player).getStorageCap();
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

    public long getLastUpdated(Player player){
        return getAutoMiner(player).getLastUpdated();
    }

    public void setLastUpdated(Player player){
        getAutoMiner(player).setLastUpdated(System.currentTimeMillis());
    }

    public void addProgress(Player player, long secondsPassed){
        AutoMinerData autoMiner = getAutoMiner(player);

        BaseBlock baseBlock = blockTypeRegistryService.getBaseBlock(getAutoMiner(player).getSelectedBlock());

        float blockHardness = baseBlock.health();
        if(autoMiner.getPickaxeId() == null) return;
        BasePickaxeItem basePickaxeItem =(BasePickaxeItem) customItemRegistryService.getBaseItemById(autoMiner.getPickaxeId());
        if(basePickaxeItem == null) return;
        float progressPerTick = basePickaxeItem.getComponent(StatsComponent.class).getStatAmount(Stats.MINING_SPEED);

        if(progressPerTick > blockHardness / MiningProgress.MAX_SPEED_DENOMINATION)
            progressPerTick = blockHardness / MiningProgress.MAX_SPEED_DENOMINATION;

        float storedProgress = autoMiner.getStoredProgress();

        autoMiner.setStoredProgress(storedProgress + (progressPerTick * 20) * secondsPassed);

        int blocksMined = (int) (storedProgress / blockHardness);

        if (blocksMined > 0) {
            autoMiner.setStoredProgress(storedProgress - blocksMined * blockHardness);
            autoMiner.addItemStorage(baseBlock.blockDropId(), blocksMined);
            setLastUpdated(player);
        }
    }

    public boolean isStorageFull(Player player) {
        return getAutoMiner(player).isFull();
    }

    public void addOfflineProgress(Player player){
        if(getAutoMinerBlockType(player) == null || hasAutoMinerPickaxe(player) || isStorageFull(player)) {
            setLastUpdated(player);
            return;
        }
        int offlineTimeCap = getAutoMiner(player).getOfflineProgress();
        long secondsSinceLastUpdate = Math.min(offlineTimeCap * 60L, System.currentTimeMillis()/1000 - getLastUpdated(player)/1000);
        addProgress(player, secondsSinceLastUpdate);
        player.sendMessage(TextConversions.parse("<gold>Miner has made progress while you were offline!"));
    }

    public void attemptAutomine(Player player, int progressTime){
        if(getAutoMinerBlockType(player) == null || hasAutoMinerPickaxe(player) || isStorageFull(player)) return;
        addProgress(player, progressTime);
    }
}
