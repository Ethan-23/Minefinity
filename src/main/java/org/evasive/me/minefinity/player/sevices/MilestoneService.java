package org.evasive.me.minefinity.player.sevices;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.resourceblock.service.ResourceData;

import java.util.List;

public class MilestoneService {

    private final PlayerManager playerManager;
    private final DirtyPlayerService dirtyPlayerService = Minefinity.getCore().getDirtyPlayerService();

    public MilestoneService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public ResourceData getResourceData(Player player, BlockType blockType) {
        return playerManager.get(player).getBlockMilestones().getResourceData(blockType);
    }

    public int getTierProgress(Player player, BlockType blockType) {
        return getResourceData(player, blockType).getProgress();
    }

    public void increaseTierProgress(Player player, BlockType blockType, int amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        getResourceData(player, blockType).setProgress(getTierProgress(player, blockType) + amount);
        if(checkTierUp(player, blockType))
            increaseTier(player, blockType, 1);
    }

    private boolean checkTierUp(Player player, BlockType blockType) {
        List<Integer> milestones = blockType.getBlock().milestoneUnlocks();
        if(getTier(player, blockType) >= milestones.size()) return false;
        return getResourceData(player, blockType).getProgress() >= blockType.getBlock().milestoneUnlocks().get(getTier(player, blockType) - 1);
    }

    public void setTierProgress(Player player, BlockType blockType, int amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        getResourceData(player, blockType).setProgress(amount);
    }

    public int getBlockMined(Player player, BlockType blockType) {
        return getResourceData(player, blockType).getBlockMined();
    }

    public void increaseBlocksMined(Player player, BlockType blockType, int amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        getResourceData(player, blockType).setBlockMined(getBlockMined(player, blockType) + amount);
    }

    public void setBlockMined(Player player, BlockType blockType, int amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        getResourceData(player, blockType).setBlockMined(amount);
    }

    public int getTier(Player player, BlockType blockType) {
        return getResourceData(player, blockType).getTier();
    }

    public void increaseTier(Player player, BlockType blockType, int amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        getResourceData(player, blockType).setTier(getTier(player, blockType) + amount);
    }

    public void setTier(Player player, BlockType blockType, int amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        getResourceData(player, blockType).setTier(amount);
    }

}
