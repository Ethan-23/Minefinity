package org.evasive.me.minefinity.mining.abilities.criticalfracture.data;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

public class Critical {
    private boolean hit;
    private final Location center;
    private BukkitTask repeatingTask;
    private long creationTime;

    private final double RADIUS = 0.1f;

    public Critical(boolean hit, Location center, BukkitTask repeatingTask) {
        this.hit = hit;
        this.center = center;
        this.repeatingTask = repeatingTask;
        this.creationTime = System.currentTimeMillis();
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public Location getCenter() {
        return center;
    }

    public double getRadius() {
        return RADIUS;
    }

    public boolean isInside(Location hitLocation) {
        return hitLocation.distanceSquared(center) <= RADIUS * RADIUS;
    }

    public BukkitTask getRepeatingTask() {
        return repeatingTask;
    }

    public void setRepeatingTask(BukkitTask repeatingTask) {
        this.repeatingTask = repeatingTask;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime() {
        this.creationTime = System.currentTimeMillis();
    }

}
