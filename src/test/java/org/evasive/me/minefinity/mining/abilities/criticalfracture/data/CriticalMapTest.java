package org.evasive.me.minefinity.mining.abilities.criticalfracture.data;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriticalMapTest {

    private CriticalMap map;
    private UUID player;
    private Location loc;

    @BeforeEach
    void setUp() {
        map = new CriticalMap();
        player = UUID.randomUUID();
        loc = new Location(null, 1, 2, 3);
    }

    @Test
    void anUntrackedPlayerHasNoCritical() {
        assertFalse(map.containsCritical(player));
        assertNull(map.getCritical(player));
    }

    @Test
    void addCriticalStoresAnUnhitCriticalAtTheGivenLocation() {
        BukkitTask task = mock(BukkitTask.class);
        map.addCritical(player, loc, task);

        assertTrue(map.containsCritical(player));
        Critical critical = map.getCritical(player);
        assertNotNull(critical);
        assertFalse(critical.isHit(), "a freshly added critical starts un-hit");
        assertSame(loc, critical.getCenter());
        assertSame(task, critical.getRepeatingTask());
    }

    @Test
    void removeCriticalCancelsTheParticleTaskAndForgetsThePlayer() {
        BukkitTask task = mock(BukkitTask.class);
        map.addCritical(player, loc, task);

        map.removeCritical(player);

        verify(task).cancel();
        assertFalse(map.containsCritical(player));
        assertNull(map.getCritical(player));
    }

    @Test
    void removeCriticalToleratesANullRepeatingTask() {
        // CriticalFracture legitimately parks a null task via hitCritical(uuid, null); a later remove
        // must not trip over the null-guard and NPE.
        map.addCritical(player, loc, null);

        assertDoesNotThrow(() -> map.removeCritical(player));
        assertFalse(map.containsCritical(player));
    }

    @Test
    void removeCriticalOnAnUnknownPlayerBlowsUp() {
        // There is no containsKey guard: get(uuid) returns null and .getRepeatingTask() dereferences it.
        assertThrows(NullPointerException.class, () -> map.removeCritical(UUID.randomUUID()));
    }

    @Test
    void hitCriticalFlipsTheHitFlagAndSwapsInTheNewTask() {
        BukkitTask original = mock(BukkitTask.class);
        BukkitTask replacement = mock(BukkitTask.class);
        map.addCritical(player, loc, original);

        map.hitCritical(player, replacement);

        assertTrue(map.getCritical(player).isHit());
        assertSame(replacement, map.getCritical(player).getRepeatingTask());
        verify(original, never()).cancel();   // hitCritical only swaps the reference; it never cancels the old task
    }

    @Test
    void hitCriticalAcceptsANullTaskAsUsedByTheAbility() {
        BukkitTask original = mock(BukkitTask.class);
        map.addCritical(player, loc, original);

        map.hitCritical(player, null);

        assertTrue(map.getCritical(player).isHit());
        assertNull(map.getCritical(player).getRepeatingTask());
    }

    @Test
    void hitCriticalOnAnUnknownPlayerBlowsUp() {
        assertThrows(NullPointerException.class,
                () -> map.hitCritical(UUID.randomUUID(), mock(BukkitTask.class)));
    }

    @Test
    void addingASecondCriticalOverwritesTheFirstWithoutCancellingItsTask() {
        // Documents a leak: re-adding for the same player replaces the Critical but never cancels the
        // previous BukkitTask, so a still-running particle task is orphaned in the scheduler.
        BukkitTask firstTask = mock(BukkitTask.class);
        BukkitTask secondTask = mock(BukkitTask.class);
        Location secondLoc = new Location(null, 9, 9, 9);

        map.addCritical(player, loc, firstTask);
        map.addCritical(player, secondLoc, secondTask);

        assertSame(secondLoc, map.getCritical(player).getCenter());
        assertSame(secondTask, map.getCritical(player).getRepeatingTask());
        verify(firstTask, never()).cancel();
    }

    @Test
    void criticalsAreTrackedPerPlayer() {
        UUID other = UUID.randomUUID();
        map.addCritical(player, loc, mock(BukkitTask.class));

        assertTrue(map.containsCritical(player));
        assertFalse(map.containsCritical(other));
    }

    @Test
    void hitThenRemoveWithANulledTaskIsTheRealAbilityLifecycle() {
        // Mirrors CriticalFracture end to end: on a hit the ability cancels the particle task and nulls
        // it via hitCritical, then onBreak removes the entry. That sequence must not throw.
        BukkitTask particle = mock(BukkitTask.class);
        map.addCritical(player, loc, particle);
        map.getCritical(player).getRepeatingTask().cancel();   // ability cancels before nulling
        map.hitCritical(player, null);

        assertDoesNotThrow(() -> map.removeCritical(player));
        assertFalse(map.containsCritical(player));
    }
}
