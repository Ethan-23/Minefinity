package org.evasive.me.minefinity.mining.milestones;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.data.MilestoneTier;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MiningMilestoneServiceTest {

    private static final String BLOCK = "Beans";

    private Player player;
    private MiningBlockMilestones milestones;
    private StatsService statsService;
    private MiningMilestoneNotifier miningMilestoneNotifier;
    private MiningMilestoneService service;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        milestones = new MiningBlockMilestones();

        PlayerData playerData = mock(PlayerData.class);
        when(playerData.get(MiningBlockMilestones.class)).thenReturn(milestones);

        PlayerDataService playerDataService = mock(PlayerDataService.class);
        when(playerDataService.getPlayerData(player)).thenReturn(playerData);

        // Tier 1 unlocks at 100 progress, tier 2 at 200. Empty stat maps keep the notifier path simple.
        BaseBlock baseBlock = new BaseBlock(BLOCK, Material.STONE, 1, 100, "drop", "special", 0f,
                List.of(new MilestoneTier(100, Map.of()), new MilestoneTier(200, Map.of())));
        BlockTypeRegistryService blockTypeRegistryService = mock(BlockTypeRegistryService.class);
        when(blockTypeRegistryService.getBaseBlock(BLOCK)).thenReturn(baseBlock);

        statsService = mock(StatsService.class);
        miningMilestoneNotifier = mock(MiningMilestoneNotifier.class);

        service = new MiningMilestoneService(playerDataService, blockTypeRegistryService, statsService, miningMilestoneNotifier);
    }

    @Test
    void getTierProgress_returnsStoredProgress() {
        milestones.changeProgress(BLOCK, 40);
        assertEquals(40, service.getTierProgress(player, BLOCK));
    }

    @Test
    void increaseTierProgress_belowThreshold_doesNotTierUp() {
        service.increaseTierProgress(player, BLOCK, 50);   // 0 -> 50, threshold is 100

        assertEquals(50, service.getTierProgress(player, BLOCK));
        assertEquals(0, service.getTier(player, BLOCK));
        verify(statsService, never()).recalculateStats(player);
    }

    @Test
    void increaseTierProgress_reachingThreshold_tiersUp() {
        service.increaseTierProgress(player, BLOCK, 100);  // 0 -> 100, hits the threshold

        assertEquals(1, service.getTier(player, BLOCK));
        verify(statsService).recalculateStats(player);
    }

    @Test
    void increaseTierProgress_reachingThreshold_overflow() {
        service.increaseTierProgress(player, BLOCK, 101);  // 0 -> 100, hits the threshold

        assertEquals(1, service.getTier(player, BLOCK));
        verify(statsService).recalculateStats(player);
    }

    @Test
    void increaseTierProgress_reachingThreshold_tiersUpDouble() {
        service.increaseTierProgress(player, BLOCK, 250);  // 0 -> 100, hits the threshold

        assertEquals(2, service.getTier(player, BLOCK));
        verify(statsService).recalculateStats(player);
    }

    @Test
    void tieringUp_notifiesThePlayer() {
        service.increaseTierProgress(player, BLOCK, 100);

        verify(miningMilestoneNotifier).tierUp(eq(player), eq(BLOCK), anyInt(), anyInt(), any());
    }
}
