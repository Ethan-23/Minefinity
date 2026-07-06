package org.evasive.me.minefinity.mining.abilities.structuredforce;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.mining.abilities.AbilityNotifier;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.mining.abilities.structuredforce.data.StreakMap;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class StructuredForceTest {

    private StreakMap streaks;
    private AbilityNotifier abilityNotifier;
    private StructuredForce ability;
    private Player player;
    private UUID uuid;
    private BaseBlock block;

    @BeforeEach
    void setUp() {
        streaks = new StreakMap();
        abilityNotifier = mock(AbilityNotifier.class);
        ability = new StructuredForce(streaks, abilityNotifier);
        player = mock(Player.class);
        uuid = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(uuid);
        block = new BaseBlock("Coal", Material.COAL_ORE, 2, 100, "drop", null, 0f, List.of());
    }

    @Test
    void onHitWithNoActiveStreakLeavesSpeedAlone() {
        StatsContext stats = new StatsContext();

        ability.onHit(new HitContext(player, block, stats));

        assertEquals(0f, stats.getSpeed(), 0f, "no streak recorded yet -> no speed bonus");
    }

    @Test
    void onHitAddsSpeedScaledByTheCurrentStreak() {
        streaks.addStreak(uuid, block);   // streak 1
        StatsContext stats = new StatsContext();

        ability.onHit(new HitContext(player, block, stats));

        assertEquals(0.02f, stats.getSpeed(), 1e-6, "streak 1 -> min(5, 1 * 0.02)");
    }

    @Test
    void onHitSpeedBonusIsCappedAtFive() {
        for (int i = 0; i < 300; i++) {
            streaks.addStreak(uuid, block);   // streak 300 -> 300 * 0.02 == 6, capped to 5
        }
        StatsContext stats = new StatsContext();

        ability.onHit(new HitContext(player, block, stats));

        assertEquals(5f, stats.getSpeed(), 1e-6, "the bonus is Math.min(5, streak * 0.02)");
    }

    @Test
    void onBreakGrowsTheStreakAndNotifiesWithTheCount() {
        ability.onBreak(new BreakContext(player, block, new StatsContext()));
        assertEquals(1, streaks.getStreak(uuid));
        verify(abilityNotifier).abilityNotification(player, PickaxeAbilities.STRUCTURED_FORCE, "Streak: <gray>1");

        ability.onBreak(new BreakContext(player, block, new StatsContext()));
        assertEquals(2, streaks.getStreak(uuid));
        verify(abilityNotifier).abilityNotification(player, PickaxeAbilities.STRUCTURED_FORCE, "Streak: <gray>2");
    }

    @Test
    void onBreakOnADifferentBlockResetsTheStreak() {
        BaseBlock other = new BaseBlock("Iron", Material.IRON_ORE, 2, 100, "drop", null, 0f, List.of());

        ability.onBreak(new BreakContext(player, block, new StatsContext()));
        ability.onBreak(new BreakContext(player, block, new StatsContext()));   // streak 2
        ability.onBreak(new BreakContext(player, other, new StatsContext()));

        assertEquals(1, streaks.getStreak(uuid));
    }
}
