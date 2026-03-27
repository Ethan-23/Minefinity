package org.evasive.me.minefinity.mining.abilities.criticalfracture.data;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CriticalMap {

    private final Map<UUID, Critical> criticalMap;

    public CriticalMap() {
        this.criticalMap = new HashMap<>();
    }

    public void addCritical(UUID uuid, Location location, BukkitTask task) {
        this.criticalMap.put(uuid, new Critical(false, location, task));
    }

    public void removeCritical(UUID uuid) {
        BukkitTask particleTask = criticalMap.get(uuid).getRepeatingTask();
        if(particleTask != null)
            particleTask.cancel();
        this.criticalMap.remove(uuid);
    }

    public void hitCritical(UUID uuid, BukkitTask task) {
        this.criticalMap.get(uuid).setHit(true);
        this.criticalMap.get(uuid).setRepeatingTask(task);
    }

    public Critical getCritical(UUID uuid) {
        return this.criticalMap.get(uuid);
    }

    public boolean containsCritical(UUID uuid) {
        return this.criticalMap.containsKey(uuid);
    }

}
