package org.evasive.me.minefinity.mining.abilities.criticalfracture;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.evasive.me.minefinity.mining.abilities.criticalfracture.data.CriticalMap;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriticalFractureTest {

    private CriticalMap criticalMap;
    private CriticalFracture ability;
    private Player player;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        criticalMap = new CriticalMap();
        ability = new CriticalFracture(criticalMap);
        player = mock(Player.class);
        uuid = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(uuid);
    }

    private HitContext hitWith(StatsContext stats) {
        return new HitContext(player, null, stats);   // applyStats only needs the uuid + stats
    }

    @Test
    void applyStatsGivesTheSpeedBonusOnlyOnceACritHasBeenHit() {
        criticalMap.addCritical(uuid, new Location(null, 0, 0, 0), mock(BukkitTask.class));
        criticalMap.hitCritical(uuid, null);   // mark the crit point as struck

        StatsContext stats = new StatsContext();
        stats.addSpeed(10f);
        ability.applyStats(hitWith(stats));

        assertEquals(12.5f, stats.getSpeed(), 1e-4, "10 * 1.25 once the crit point was hit");
    }

    @Test
    void applyStatsDoesNothingWhileTheCritIsStillUnhit() {
        criticalMap.addCritical(uuid, new Location(null, 0, 0, 0), mock(BukkitTask.class));
        // present but not hit yet

        StatsContext stats = new StatsContext();
        stats.addSpeed(10f);
        ability.applyStats(hitWith(stats));

        assertEquals(10f, stats.getSpeed(), 1e-6, "an un-hit crit grants no speed");
    }

    @Test
    void applyStatsDoesNothingWithNoCritForThePlayer() {
        StatsContext stats = new StatsContext();
        stats.addSpeed(10f);

        ability.applyStats(hitWith(stats));

        assertEquals(10f, stats.getSpeed(), 1e-6);
    }
}
