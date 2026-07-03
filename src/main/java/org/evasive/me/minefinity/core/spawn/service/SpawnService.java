package org.evasive.me.minefinity.core.spawn.service;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.evasive.me.minefinity.core.config.LocationConfig;

import java.util.Objects;

public class SpawnService {

    private Location spawnLocation;
    private final LocationConfig locationConfig;

    public SpawnService(LocationConfig locationConfig) {
        this.locationConfig = locationConfig;

        this.spawnLocation = loadSpawnLocation();

        if(spawnLocation == null){

            Location location = Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation();
            this.spawnLocation = location;
            saveNewSpawnLocation(location);
        }

    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        saveNewSpawnLocation(location);
    }

    private void saveNewSpawnLocation(Location location){
        locationConfig.getConfig().set("spawn", location);
        locationConfig.save();
    }

    private Location loadSpawnLocation(){
        return locationConfig.getConfig().getLocation("spawn");
    }

}
