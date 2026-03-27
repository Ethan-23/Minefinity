package org.evasive.me.minefinity.core.spawn.service;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.evasive.me.minefinity.Minefinity;

import java.util.Objects;

public class SpawnService {

    private Location spawnLocation;

    public SpawnService() {
        spawnLocation = Minefinity.getCore().getConfig().getLocation("spawn");

        if(spawnLocation == null){

            Location location = Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation();
            this.spawnLocation = location;
            saveNewSpawnLocation(location);
        }else{
            this.spawnLocation = loadSpawnLocation();
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
        Minefinity.getCore().getConfig().set("spawn", location);
        Minefinity.getCore().saveConfig();
    }

    private Location loadSpawnLocation(){
        return Minefinity.getCore().getConfig().getLocation("spawn");
    }

}
