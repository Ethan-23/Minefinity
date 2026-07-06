package org.evasive.me.minefinity.mining.milestones;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.data.MilestoneTier;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.StatContributor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contributes the stat bonuses a player has unlocked through block-mining milestones.
 * Owned by mining; registered with playerdata's stat registry at startup.
 */
public class MiningMilestoneStatContributor implements StatContributor {

    private final PlayerDataService playerDataService;
    private final BlockTypeRegistryService blockTypeRegistryService;

    public MiningMilestoneStatContributor(PlayerDataService playerDataService, BlockTypeRegistryService blockTypeRegistryService) {
        this.playerDataService = playerDataService;
        this.blockTypeRegistryService = blockTypeRegistryService;
    }

    @Override
    public Map<Stats, Integer> contribute(Player player) {
        Map<Stats, Integer> statsMap = new HashMap<>();

        List<BaseBlock> blockList = blockTypeRegistryService.getAllBlocks();

        for (BaseBlock baseBlock : blockList) {
            List<MilestoneTier> milestoneTiers = baseBlock.milestoneUnlocks();
            String blockId = baseBlock.name();
            int tier = playerDataService.getPlayerData(player.getUniqueId()).get(MiningBlockMilestones.class).getTier(blockId);

            for (int i = 1; i <= tier; i++) {
                Map<String, Integer> statsStringMap = milestoneTiers.get(i - 1).stats();
                for (String statId : statsStringMap.keySet()) {
                    Stats stat = Stats.getStat(statId);
                    if(stat == null) continue;
                    statsMap.merge(stat, statsStringMap.get(statId), Integer::sum);
                }
            }
        }
        return statsMap;
    }
}
