package org.evasive.me.minefinity.mining.abilities.metaldetector;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.mining.abilities.AbilityNotifier;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.mining.context.BreakContext;
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
    void onBreakAddsFiveToSpecialChance() {
        StatsContext stats = new StatsContext();   // baseline 1

        metalDetector.onBreak(new BreakContext(player, null, stats));

        assertEquals(6, stats.getSpecialChance(), "1 baseline + 5 from the detector");
    }

    @Test
    void whenTheDropIsSpecialTheProcNotificationIsSent() {
        StatsContext stats = new StatsContext();
        stats.setSpecialDrop(true);

        metalDetector.onBreak(new BreakContext(player, null, stats));

        verify(abilityNotifier).abilityNotification(player, PickaxeAbilities.METAL_DETECT, "Proc");
    }

    @Test
    void whenTheDropIsNotSpecialNoNotificationIsSent() {
        StatsContext stats = new StatsContext();   // specialDrop defaults to false

        metalDetector.onBreak(new BreakContext(player, null, stats));

        verify(abilityNotifier, never()).abilityNotification(any(), any(), any());
    }

    @Test
    void theChanceBoostIsAppliedRegardlessOfWhetherTheDropWasSpecial() {
        StatsContext stats = new StatsContext();
        stats.setSpecialDrop(false);

        metalDetector.onBreak(new BreakContext(player, null, stats));

        // The +5 happens before the isSpecialDrop() branch, so it lands even on a non-special break.
        assertEquals(6, stats.getSpecialChance());
    }
}
