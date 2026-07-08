package org.evasive.me.minefinity.mining.milestones;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.data.MilestoneTier;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MiningMilestoneStatContributorTest {

    private Player player;
    private MiningBlockMilestones milestones;
    private BlockTypeRegistryService blockTypeRegistryService;
    private MiningMilestoneStatContributor contributor;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(uuid);

        milestones = new MiningBlockMilestones();

        PlayerData playerData = mock(PlayerData.class);
        when(playerData.get(MiningBlockMilestones.class)).thenReturn(milestones);

        PlayerDataService playerDataService = mock(PlayerDataService.class);
        when(playerDataService.getPlayerData(uuid)).thenReturn(playerData);

        blockTypeRegistryService = mock(BlockTypeRegistryService.class);

        contributor = new MiningMilestoneStatContributor(playerDataService, blockTypeRegistryService);
    }

    private BaseBlock block(String name, MilestoneTier... tiers) {
        return new BaseBlock(name, Material.STONE, 1, 100, "drop", null, 0f, List.of(tiers));
    }

    @Test
    void aPlayerAtTierZeroContributesNothing() {
        when(blockTypeRegistryService.getAllBlocks()).thenReturn(List.of(
                block("Coal", new MilestoneTier(100, Map.of(Stats.MINING_SPEED.name(), 5)))));
        // the milestone tier defaults to 0, so the per-tier loop never runs

        assertTrue(contributor.contribute(player).isEmpty());
    }

    @Test
    void tierOneContributesThatTiersStats() {
        when(blockTypeRegistryService.getAllBlocks()).thenReturn(List.of(
                block("Coal", new MilestoneTier(100, Map.of(Stats.MINING_SPEED.name(), 5)))));
        milestones.changeTier("Coal", 1);

        assertEquals(5, contributor.contribute(player).get(Stats.MINING_SPEED));
    }

    @Test
    void higherTiersAccumulateEveryUnlockedTierNotJustTheCurrentOne() {
        when(blockTypeRegistryService.getAllBlocks()).thenReturn(List.of(
                block("Coal",
                        new MilestoneTier(100, Map.of(Stats.MINING_SPEED.name(), 2)),
                        new MilestoneTier(200, Map.of(Stats.MINING_SPEED.name(), 3)))));
        milestones.changeTier("Coal", 2);

        // both tier 1 AND tier 2 are summed: 2 + 3
        assertEquals(5, contributor.contribute(player).get(Stats.MINING_SPEED));
    }

    @Test
    void statsMergeAcrossDifferentBlocks() {
        when(blockTypeRegistryService.getAllBlocks()).thenReturn(List.of(
                block("Coal", new MilestoneTier(100, Map.of(Stats.MINING_FORTUNE.name(), 1))),
                block("Iron", new MilestoneTier(100, Map.of(Stats.MINING_FORTUNE.name(), 4)))));
        milestones.changeTier("Coal", 1);
        milestones.changeTier("Iron", 1);

        assertEquals(5, contributor.contribute(player).get(Stats.MINING_FORTUNE));
    }

    @Test
    void unrecognisedStatIdsAreSkippedButValidOnesStillLand() {
        when(blockTypeRegistryService.getAllBlocks()).thenReturn(List.of(
                block("Coal", new MilestoneTier(100, Map.of(
                        "TOTALLY_NOT_A_STAT", 999,
                        Stats.BREAKING_POWER.name(), 3)))));
        milestones.changeTier("Coal", 1);

        Map<Stats, Integer> result = contributor.contribute(player);

        assertEquals(3, result.get(Stats.BREAKING_POWER));
        assertEquals(1, result.size(), "the bogus stat id is dropped, not merged under some fallback");
    }

    @Test
    void aBlockLeftAtTierZeroIsIgnoredEvenWhileOthersContribute() {
        when(blockTypeRegistryService.getAllBlocks()).thenReturn(List.of(
                block("Coal", new MilestoneTier(100, Map.of(Stats.MINING_SPEED.name(), 5))),
                block("Iron", new MilestoneTier(100, Map.of(Stats.MINING_SPEED.name(), 9)))));
        milestones.changeTier("Iron", 1);   // Coal stays at tier 0

        assertEquals(9, contributor.contribute(player).get(Stats.MINING_SPEED),
                "only Iron's tier-1 bonus counts");
    }

    @Test
    void aStoredTierBeyondTheDefinedMilestonesBlowsUp() {
        // If a block definition shrinks (or a save carries an over-large tier), get(i - 1) walks off the
        // end of the milestone list. Documents that there is no bounds guard between the stored tier and
        // the number of defined milestone tiers.
        when(blockTypeRegistryService.getAllBlocks()).thenReturn(List.of(
                block("Coal", new MilestoneTier(100, Map.of(Stats.MINING_SPEED.name(), 5)))));
        milestones.changeTier("Coal", 2);   // only one tier is defined, but the player is recorded at tier 2

        assertThrows(IndexOutOfBoundsException.class, () -> contributor.contribute(player));
    }
}
