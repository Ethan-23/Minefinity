package org.evasive.me.minefinity.mining.abilities.criticalfracture.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

class CriticalTest {

    private World world;
    private Location center;
    private BukkitTask task;
    private Critical critical;

    @BeforeEach
    void setUp() {
        world = mock(World.class);
        center = new Location(world, 0, 0, 0);
        task = mock(BukkitTask.class);
        critical = new Critical(true, center, task);
    }

    @Test
    void gettersReflectTheConstructorArguments() {
        assertTrue(critical.isHit());
        assertSame(center, critical.getCenter());
        assertSame(task, critical.getRepeatingTask());
    }

    @Test
    void hitFlagAndRepeatingTaskAreMutable() {
        critical.setHit(false);
        assertFalse(critical.isHit());

        BukkitTask replacement = mock(BukkitTask.class);
        critical.setRepeatingTask(replacement);
        assertSame(replacement, critical.getRepeatingTask());
    }

    @Test
    void radiusIsTheDeclaredTenthOfABlock() {
        assertEquals(0.1f, critical.getRadius(), 1e-6);
    }

    @Test
    void aPointWellInsideTheRadiusCounts() {
        Location near = new Location(world, 0.05, 0, 0);   // dist^2 = 0.0025 <= 0.01
        assertTrue(critical.isInside(near));
    }

    @Test
    void aPointOutsideTheRadiusDoesNotCount() {
        Location far = new Location(world, 0.2, 0, 0);      // dist^2 = 0.04 > 0.01
        assertFalse(critical.isInside(far));
    }

    @Test
    void thePointExactlyOnTheRadiusIsTreatedAsInside() {
        Location edge = new Location(world, 0.1, 0, 0);     // dist^2 == radius^2 (inclusive <=)
        assertTrue(critical.isInside(edge),
                "isInside uses <=, so the boundary point is considered a hit");
    }

    @Test
    void creationTimeIsStampedAtConstruction() {
        long before = System.currentTimeMillis();
        Critical fresh = new Critical(false, center, task);
        long after = System.currentTimeMillis();

        assertTrue(fresh.getCreationTime() >= before && fresh.getCreationTime() <= after,
                "creation time should be captured during construction");
    }

    @Test
    void resettingCreationTimeMovesItForward() {
        long original = critical.getCreationTime();
        critical.setCreationTime();
        assertTrue(critical.getCreationTime() >= original);
    }
}
