package org.evasive.me.minefinity.mining.milestones;
import org.evasive.me.minefinity.core.data.MilestoneTier;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;
import org.evasive.me.minefinity.core.data.ResourceData;

import java.util.List;
import java.util.Map;

public class MilestoneService {

    private final PlayerDataService playerDataService;
    private final BlockTypeRegistryService blockTypeRegistryService;
    private final StatsService statsService;

    public MilestoneService(PlayerDataService playerDataService, BlockTypeRegistryService blockTypeRegistryService, StatsService statsService) {
        this.playerDataService = playerDataService;
        this.blockTypeRegistryService = blockTypeRegistryService;
        this.statsService = statsService;
    }

    public ResourceData getResourceData(Player player, String blockId) {
        return playerDataService.getPlayerData(player.getUniqueId()).get(BlockMilestone.class).getResourceData(blockId);
    }

    public int getTierProgress(Player player, String blockId) {
        return getResourceData(player, blockId).getProgress();
    }

    public void increaseTierProgress(Player player, String blockId, int amount) {
        getResourceData(player, blockId).setProgress(getTierProgress(player, blockId) + amount);
        if(!checkTierUp(player, blockId))
            return;
        increaseTier(player, blockId, 1);
    }

    private boolean checkTierUp(Player player, String blockId) {
        BaseBlock baseBlock = blockTypeRegistryService.getBaseBlock(blockId);
        List<MilestoneTier> milestones = baseBlock.milestoneUnlocks();
        int currentTier = getTier(player, blockId);
        if(currentTier >= milestones.size()) return false;
        return getResourceData(player, blockId).getProgress() >= milestones.get(currentTier).amount();
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
        int currentTier = getTier(player, blockId);
        getResourceData(player, blockId).setTier(currentTier + amount);
        statsService.recalculateStats(player);
        announceTierUp(player, blockId);
    }

    public void setTier(Player player, String blockId, int amount) {
        getResourceData(player, blockId).setTier(amount);
    }

    private void announceTierUp(Player player, String blockId) {
        int tier = getTier(player, blockId);
        MilestoneTier milestoneTier = blockTypeRegistryService.getBaseBlock(blockId).milestoneUnlocks().get(tier - 1);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
        player.sendMessage(TextConversions.parse("<yellow>"  + TextConversions.formatItemName(blockId) +"<gold> Milestone Increased <yellow>" + TextConversions.intToRoman(Math.max(0, tier - 1)) + " <gold>-> <yellow>" + TextConversions.intToRoman(tier)));
        Map<String, Integer> rewardStatsMap = milestoneTier.stats();

        for(Map.Entry<String, Integer> entry : rewardStatsMap.entrySet()) {
            player.sendMessage(TextConversions.parse("  <gray>+" + entry.getValue() + " " + Stats.valueOf(entry.getKey()).getDisplay()));
        }

    }
}
