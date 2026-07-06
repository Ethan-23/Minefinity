package org.evasive.me.minefinity.mining.data;

import org.bukkit.Location;
import org.evasive.me.minefinity.mining.utils.AnimationIDs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MiningDataMapTest {

    private AnimationIDs animationIDs;
    private MiningDataMap map;
    private Location loc;
    private UUID player;

    @BeforeEach
    void setUp() {
        animationIDs = new AnimationIDs();
        map = new MiningDataMap(animationIDs);
        loc = new Location(null, 1, 2, 3);
        player = UUID.randomUUID();
    }

    @Test
    void addThenReadBackProgressAndAnimationId() {
        map.addMiningData(loc, player, new MiningBlockData(77, 0f));
        map.increaseBlockProgress(loc, player, 2.5f);
        map.increaseBlockProgress(loc, player, 1.5f);

        assertEquals(4f, map.getBlockProgress(loc, player), 1e-6);
        assertEquals(77, map.getBlockAnimationID(loc, player));
        assertTrue(map.containsBlockLocation(loc));
        assertTrue(map.containsPlayerAtLocation(loc, player));
    }

    @Test
    void addingSamePlayerTwiceDoesNotOverwriteExistingData() {
        map.addMiningData(loc, player, new MiningBlockData(1, 5f));
        map.addMiningData(loc, player, new MiningBlockData(2, 99f));   // must be ignored

        assertEquals(5f, map.getBlockProgress(loc, player), 1e-6,
                "a second add for the same player must not reset their progress");
        assertEquals(1, map.getBlockAnimationID(loc, player),
                "a second add for the same player must not swap their animation id");
    }

    @Test
    void twoPlayersAtSameLocationAreTrackedIndependently() {
        UUID other = UUID.randomUUID();
        map.addMiningData(loc, player, new MiningBlockData(1, 10f));
        map.addMiningData(loc, other, new MiningBlockData(2, 20f));

        assertEquals(10f, map.getBlockProgress(loc, player), 1e-6);
        assertEquals(20f, map.getBlockProgress(loc, other), 1e-6);
    }

    @Test
    void removeBlockPosRecyclesTheAnimationIdBackIntoThePool() {
        int freshId = animationIDs.getUniqueAnimationId();     // pull one out of the pool
        map.addMiningData(loc, player, new MiningBlockData(freshId, 0f));

        map.removeBlockPos(loc, player);

        assertEquals(freshId, animationIDs.getUniqueAnimationId(),
                "removing a mining position should release its animation id for reuse");
    }

    @Test
    void removeBlockPosDropsThePlayerEntry() {
        map.addMiningData(loc, player, new MiningBlockData(1, 0f));
        map.removeBlockPos(loc, player);

        assertFalse(map.containsPlayerAtLocation(loc, player));
    }

    @Test
    void removingTheLastMinerLeavesAStaleLocationEntry() {
        map.addMiningData(loc, player, new MiningBlockData(1, 0f));
        map.removeBlockPos(loc, player);

        // NOTE: removeBlockPos deletes the player but leaves an empty inner map behind, so the
        // location still reports as "contained" even though nobody is mining it anymore.
        assertTrue(map.containsBlockLocation(loc),
                "documents current behaviour: the location key is not cleaned up when its last miner leaves");
        assertFalse(map.containsPlayerAtLocation(loc, player));
    }

    @Test
    void containsChecksAreSafeForUnknownLocationsAndPlayers() {
        assertFalse(map.containsBlockLocation(loc));
        assertFalse(map.containsPlayerAtLocation(loc, player));

        map.addMiningData(loc, player, new MiningBlockData(1, 0f));
        assertFalse(map.containsPlayerAtLocation(loc, UUID.randomUUID()),
                "a different player at a known location is not present");
    }

    @Test
    void readingProgressForAnUntrackedLocationHasNoGuardAndThrows() {
        // There is no null-guard on the inner map lookup, so querying an unknown location blows up.
        assertThrows(NullPointerException.class, () -> map.getBlockProgress(loc, player));
    }
}
