package org.evasive.me.minefinity.mining.abilities.earlybird;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EarlyBirdTest {

    private BaseBlock blockWithBreakingPower(int breakingPower) {
        return new BaseBlock("Rock", Material.STONE, breakingPower, 100, "drop", null, 0f, List.of());
    }

    private HitContext hit(BaseBlock block, StatsContext stats) {
        return new HitContext(null, block, stats);   // EarlyBird never touches the player
    }

    @Test
    void breakingPowerOneBlocksGetTheTenPercentSpeedBoost() {
        StatsContext stats = new StatsContext();
        stats.addSpeed(10f);

        new EarlyBird().applyStats(hit(blockWithBreakingPower(1), stats));

        assertEquals(11f, stats.getSpeed(), 1e-5, "10 * 1.1 == 11");
    }

    @Test
    void higherBreakingPowerBlocksAreLeftUntouched() {
        StatsContext stats = new StatsContext();
        stats.addSpeed(10f);

        new EarlyBird().applyStats(hit(blockWithBreakingPower(2), stats));

        assertEquals(10f, stats.getSpeed(), 1e-6, "only breaking power 1 qualifies");
    }

    @Test
    void breakingPowerZeroDoesNotQualify() {
        StatsContext stats = new StatsContext();
        stats.addSpeed(10f);

        new EarlyBird().applyStats(hit(blockWithBreakingPower(0), stats));

        assertEquals(10f, stats.getSpeed(), 1e-6);
    }

    @Test
    void theBoostIsMultiplicativeSoItDoesNothingWhenSpeedIsStillZero() {
        StatsContext stats = new StatsContext();   // speed 0 -> 0 * 1.1 == 0

        new EarlyBird().applyStats(hit(blockWithBreakingPower(1), stats));

        // Documents an ordering trap: EarlyBird only helps if player speed was already added to the context.
        assertEquals(0f, stats.getSpeed(), 0f);
    }
}
