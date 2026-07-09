package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.evasive.me.minefinity.core.data.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsComponentTest {

    @Test
    void setStatStoresAndGetStatAmountReadsBack() {
        StatsComponent stats = new StatsComponent();
        stats.setStat(Stats.MINING_SPEED, 42);
        assertEquals(42, stats.getStatAmount(Stats.MINING_SPEED));
    }

    @Test
    void getStatAmountDefaultsToZeroForAnUnsetStat() {
        StatsComponent stats = new StatsComponent();
        assertEquals(0, stats.getStatAmount(Stats.MINING_FORTUNE));
    }

    @Test
    void settingAStatToZeroRemovesItRatherThanStoringZero() {
        StatsComponent stats = new StatsComponent();
        stats.setStat(Stats.MINING_SPEED, 10);
        stats.setStat(Stats.MINING_SPEED, 0);

        assertFalse(stats.getValue().containsKey(Stats.MINING_SPEED.name()),
                "0 is treated as 'clear the stat', not a stored zero");
        assertEquals(0, stats.getStatAmount(Stats.MINING_SPEED));
    }

    @Test
    void negativeStatsAreStored() {
        StatsComponent stats = new StatsComponent();
        stats.setStat(Stats.MINING_SPEED, -5);
        assertEquals(-5, stats.getStatAmount(Stats.MINING_SPEED), "negatives are allowed (a penalty part)");
    }

    @Test
    void setValueNullResetsToAnEmptyMapRatherThanNull() {
        StatsComponent stats = new StatsComponent();
        stats.setValue(null);
        assertNotNull(stats.getValue());
        assertTrue(stats.getValue().isEmpty());
    }
}
