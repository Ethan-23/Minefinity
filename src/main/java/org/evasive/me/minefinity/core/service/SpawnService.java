package org.evasive.me.minefinity.core.service;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.Minefinity;

import java.lang.module.Configuration;
import java.util.HashMap;
import java.util.Map;

public class SpawnService {

    private final Map<World, Location> spawnLocations;
    private final ConfigurationSection worldSection;

    public SpawnService() {
        spawnLocations = new HashMap<>();

        if(Minefinity.getCore().getConfig().getConfigurationSection("worlds") == null){
            Minefinity.getCore().getConfig().createSection("worlds");
            Minefinity.getCore().saveConfig();
        }

        worldSection = Minefinity.getCore().getConfig().getConfigurationSection("worlds");

        for(World world : Bukkit.getWorlds()) {
            if(worldSection != null && worldSection.contains(world.getName())) continue;
            spawnLocations.put(world, world.getSpawnLocation());
        }

    }

    public Location getSpawnLocation(World world) {
        return spawnLocations.get(world);
    }

    public void setSpawnLocation(World world, Location spawnLocation) {
        this.spawnLocations.put(world, spawnLocation);
        saveNewSpawnLocation(world, spawnLocation);
    }

    private void saveNewSpawnLocation(World world, Location location){

        String worldName = world.getName();
        worldSection.set(worldName + ".x", location.getX());
        worldSection.set(worldName + ".y", location.getY());
        worldSection.set(worldName + ".z", location.getZ());
        worldSection.set(worldName + ".yaw", location.getYaw());
        worldSection.set(worldName + ".pitch", location.getPitch());
        Minefinity.getCore().saveConfig();
    }

}
