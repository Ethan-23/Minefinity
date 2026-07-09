package org.evasive.me.minefinity.mining.abilities.metaldetector;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MetalDetectorTest {

    private Player player;
    private MetalDetector metalDetector;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        metalDetector = new MetalDetector();
    }

    @Test
    void applyStatsAddsFiveToSpecialChance() {
        StatsContext stats = new StatsContext();   // baseline 1

        metalDetector.applyStats(new HitContext(player, null, stats));

        assertEquals(6, stats.getSpecialChance(), "1 baseline + 5 from the detector");
    }

    @Test
    void applyStatsContributesChanceWithoutTouchingTheDropOutcome() {
        // MetalDetector is now a pure stat contributor: it only adds special chance and never reads
        // or sets the special-drop flag itself.
        StatsContext stats = new StatsContext();

        metalDetector.applyStats(new HitContext(player, null, stats));

        assertEquals(6, stats.getSpecialChance());
        assertFalse(stats.isSpecialDrop());
    }
}
