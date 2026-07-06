package org.evasive.me.minefinity.mining.context;

import org.evasive.me.minefinity.core.data.Stats;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StatsContextTest {

    @Test
    void freshContextHasExpectedDefaults() {
        StatsContext c = new StatsContext();

        assertEquals(0, c.getBreakingPower());
        assertEquals(0f, c.getSpeed(), 0f);
        assertEquals(0f, c.getFortune(), 0f);
        assertFalse(c.isSpecialDrop());
        // NOTE: specialChance is seeded at 1, not 0 — every break carries a baseline 1% special
        // roll even with no bonuses applied. Flagged as a likely off-by-one.
        assertEquals(1, c.getSpecialChance());
    }

    @Test
    void additiveMutatorsAccumulate() {
        StatsContext c = new StatsContext();
        c.addBreakingPower(3);
        c.addBreakingPower(2);
        c.addSpeed(1.5f);
        c.addSpeed(0.25f);
        c.addFortune(10f);
        c.addSpecialChance(4);

        assertEquals(5, c.getBreakingPower());
        assertEquals(1.75f, c.getSpeed(), 1e-6);
        assertEquals(10f, c.getFortune(), 1e-6);
        assertEquals(5, c.getSpecialChance());   // 1 baseline + 4
    }

    @Test
    void multiplySpeedScalesTheAccumulatedValue() {
        StatsContext c = new StatsContext();
        c.addSpeed(10f);
        c.multiplySpeed(1.1f);
        assertEquals(11f, c.getSpeed(), 1e-5);
    }

    @Test
    void multiplyingSpeedBeforeAnyIsAddedIsANoOp() {
        StatsContext c = new StatsContext();
        c.multiplySpeed(1.1f);   // 0 * 1.1 == 0
        // Gotcha: multiplicative buffs (e.g. EarlyBird) do nothing unless base speed was added first.
        assertEquals(0f, c.getSpeed(), 0f);
    }

    @Test
    void multiplyFortuneScalesTheAccumulatedValue() {
        StatsContext c = new StatsContext();
        c.addFortune(4f);
        c.multiplyFortune(2.5f);
        assertEquals(10f, c.getFortune(), 1e-6);
    }

    @Test
    void addStatsPullsTheThreeMiningStatsFromThePlayerMap() {
        StatsContext c = new StatsContext();
        Map<String, Integer> stats = new HashMap<>();
        stats.put(Stats.MINING_SPEED.name(), 3);
        stats.put(Stats.MINING_FORTUNE.name(), 4);
        stats.put(Stats.BREAKING_POWER.name(), 5);

        c.addStats(stats);

        assertEquals(3f, c.getSpeed(), 1e-6);
        assertEquals(4f, c.getFortune(), 1e-6);
        assertEquals(5, c.getBreakingPower());
    }

    @Test
    void addStatsWithMissingKeysLeavesValuesUntouched() {
        StatsContext c = new StatsContext();
        c.addStats(new HashMap<>());   // no keys present -> getOrDefault 0

        assertEquals(0f, c.getSpeed(), 0f);
        assertEquals(0f, c.getFortune(), 0f);
        assertEquals(0, c.getBreakingPower());
    }

    @Test
    void addStatsAddsOnTopOfExistingValues() {
        StatsContext c = new StatsContext();
        c.addSpeed(1f);
        c.addStats(Map.of(Stats.MINING_SPEED.name(), 2));

        assertEquals(3f, c.getSpeed(), 1e-6, "addStats should accumulate, not replace");
    }

    @Test
    void addStatsDoesNotTouchSpecialChance() {
        StatsContext c = new StatsContext();
        c.addStats(Map.of(Stats.MINING_SPEED.name(), 9));
        assertEquals(1, c.getSpecialChance(), "special chance is not sourced from the stat map");
    }
}
