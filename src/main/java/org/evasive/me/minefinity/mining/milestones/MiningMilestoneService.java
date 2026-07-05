package org.evasive.me.minefinity.mining.milestones;
import org.evasive.me.minefinity.core.data.MilestoneTier;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;

import java.util.List;

public class MiningMilestoneService {

    private final PlayerDataService playerDataService;
    private final BlockTypeRegistryService blockTypeRegistryService;
    private final StatsService statsService;
    private final MiningMilestoneNotifier miningMilestoneNotifier;

    public MiningMilestoneService(PlayerDataService playerDataService, BlockTypeRegistryService blockTypeRegistryService, StatsService statsService, MiningMilestoneNotifier miningMilestoneNotifier) {
        this.playerDataService = playerDataService;
        this.blockTypeRegistryService = blockTypeRegistryService;
        this.statsService = statsService;
        this.miningMilestoneNotifier = miningMilestoneNotifier;
    }

    private MiningBlockMilestones getMilestone(Player player) {
        return playerDataService.getPlayerData(player).get(MiningBlockMilestones.class);
    }

    public int getTierProgress(Player player, String blockId) {
        return getMilestone(player).getProgress(blockId);
    }

    public void increaseTierProgress(Player player, String blockId, int amount) {
        getMilestone(player).changeProgress(blockId, amount);

        boolean tieredUp = false;
        while(checkTierUp(player, blockId)) {
            increaseTier(player, blockId);
            tieredUp = true;
        }

        if(tieredUp)
            statsService.recalculateStats(player);
    }

    private boolean checkTierUp(Player player, String blockId) {
        BaseBlock baseBlock = blockTypeRegistryService.getBaseBlock(blockId);
        List<MilestoneTier> milestones = baseBlock.milestoneUnlocks();
        int currentTier = getTier(player, blockId);
        if(currentTier >= milestones.size()) return false;
        return getMilestone(player).getProgress(blockId) >= milestones.get(currentTier).amount();
    }

    public void increaseBlocksMined(Player player, String blockId, int amount) {
        getMilestone(player).changeBlocksMined(blockId, amount);
    }

    public int getTier(Player player, String blockId) {
        return getMilestone(player).getTier(blockId);
    }

    private void increaseTier(Player player, String blockId) {
        int oldTier = getMilestone(player).getTier(blockId);
        getMilestone(player).changeTier(blockId, 1);

        MilestoneTier milestoneTier = blockTypeRegistryService.getBaseBlock(blockId).milestoneUnlocks().get(oldTier);
        miningMilestoneNotifier.tierUp(player, blockId, oldTier, oldTier + 1, milestoneTier);
    }

}
