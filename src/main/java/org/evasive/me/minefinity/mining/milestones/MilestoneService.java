package org.evasive.me.minefinity.mining.milestones;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTypeRegistryService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.ResourceData;

import java.util.List;

public class MilestoneService {

    private final PlayerDataService playerDataService;
    private final BlockTypeRegistryService blockTypeRegistryService;

    public MilestoneService(PlayerDataService playerDataService, BlockTypeRegistryService blockTypeRegistryService) {
        this.playerDataService = playerDataService;
        this.blockTypeRegistryService = blockTypeRegistryService;
    }

    public ResourceData getResourceData(Player player, String blockId) {
        return playerDataService.getPlayerData(player.getUniqueId()).getBlockMilestones().getResourceData(blockId);
    }

    public int getTierProgress(Player player, String blockId) {
        return getResourceData(player, blockId).getProgress();
    }

    public void increaseTierProgress(Player player, String blockId, int amount) {
        getResourceData(player, blockId).setProgress(getTierProgress(player, blockId) + amount);
        if(checkTierUp(player, blockId))
            increaseTier(player, blockId, 1);
    }

    private boolean checkTierUp(Player player, String blockId) {
        BaseBlock baseBlock = blockTypeRegistryService.getBaseBlock(blockId);
        List<Integer> milestones = baseBlock.milestoneUnlocks();
        if(getTier(player, blockId) >= milestones.size()) return false;
        return getResourceData(player, blockId).getProgress() >= baseBlock.milestoneUnlocks().get(getTier(player, blockId));
    }

    public void setTierProgress(Player player, String blockId, int amount) {
        getResourceData(player, blockId).setProgress(amount);
    }

    public int getBlockMined(Player player, String blockId) {
        return getResourceData(player, blockId).getBlockMined();
    }

    public void increaseBlocksMined(Player player, String blockId, int amount) {
        getResourceData(player, blockId).setBlockMined(getBlockMined(player, blockId) + amount);
    }

    public void setBlockMined(Player player, String blockId, int amount) {
        getResourceData(player, blockId).setBlockMined(amount);
    }

    public int getTier(Player player, String blockId) {
        return getResourceData(player, blockId).getTier();
    }

    public void increaseTier(Player player, String blockId, int amount) {
        getResourceData(player, blockId).setTier(getTier(player, blockId) + amount);
    }

    public void setTier(Player player, String blockId, int amount) {
        getResourceData(player, blockId).setTier(amount);
    }

}
