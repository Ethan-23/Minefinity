package org.evasive.me.minefinity.mining.abilities.metaldetector;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.mining.abilities.AbilityNotifier;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MetalDetectorTest {

    private AbilityNotifier abilityNotifier;
    private Player player;
    private MetalDetector metalDetector;

    @BeforeEach
    void setUp() {
        abilityNotifier = mock(AbilityNotifier.class);   // the Bukkit/Sound side is behind this seam
        player = mock(Player.class);
        metalDetector = new MetalDetector(abilityNotifier);
    }

    @Test
    void applyStatsAddsFiveToSpecialChance() {
        StatsContext stats = new StatsContext();   // baseline 1

        metalDetector.applyStats(new HitContext(player, null, stats));

        assertEquals(6, stats.getSpecialChance(), "1 baseline + 5 from the detector");
    }

    @Test
    void applyStatsContributesChanceWithoutTouchingTheDropOutcome() {
        // applyStats never reads or sets the special-drop flag — it only contributes chance, and it does
        // so before the roll (so the +5 can actually change this break's outcome).
        StatsContext stats = new StatsContext();

        metalDetector.applyStats(new HitContext(player, null, stats));

        assertEquals(6, stats.getSpecialChance());
        assertFalse(stats.isSpecialDrop());
        verifyNoInteractions(abilityNotifier);
    }

    @Test
    void onBreakSendsTheProcNotificationWhenTheDropWasSpecial() {
        StatsContext stats = new StatsContext();
        stats.setSpecialDrop(true);

        metalDetector.onBreak(new BreakContext(player, null, stats));

        verify(abilityNotifier).abilityNotification(player, PickaxeAbilities.METAL_DETECT, "Proc");
    }

    @Test
    void onBreakStaysQuietWhenTheDropWasNotSpecial() {
        StatsContext stats = new StatsContext();   // specialDrop defaults to false

        metalDetector.onBreak(new BreakContext(player, null, stats));

        verify(abilityNotifier, never()).abilityNotification(any(), any(), any());
    }

    @Test
    void onBreakNoLongerContributesSpecialChance() {
        // The chance boost moved to applyStats; onBreak is now a pure reaction and must not re-add it,
        // otherwise the detector would double-count on the breaking swing.
        StatsContext stats = new StatsContext();
        stats.setSpecialDrop(true);

        metalDetector.onBreak(new BreakContext(player, null, stats));

        assertEquals(1, stats.getSpecialChance(), "onBreak must not touch special chance anymore");
    }
}
