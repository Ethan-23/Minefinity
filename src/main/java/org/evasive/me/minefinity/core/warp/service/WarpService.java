package org.evasive.me.minefinity.core.warp.service;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.core.config.LocationConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WarpService {

    Map<String, Location> warps = new HashMap<>();;
    private final LocationConfig locationConfig;
    private final ConfigurationSection warpConfiguration;

    public WarpService(LocationConfig locationConfig) {
        this.locationConfig = locationConfig;

        ConfigurationSection section = locationConfig.getConfig().getConfigurationSection("warps");
        if (section == null) {
            section = locationConfig.getConfig().createSection("warps");
            locationConfig.save();
        }
        this.warpConfiguration = section;

        loadWarpLocation();
    }

    public void addWarpLocation(String warpName, Location location){
        warps.put(warpName.toLowerCase(), location);
    }

    public void removeWarpLocation(String warpName){
        warps.remove(warpName.toLowerCase());
    }

    public boolean containsWarpLocation(String warpName){
        return warps.containsKey(warpName.toLowerCase());
    }

    public Location getWarpLocation(String warpName){
        return warps.get(warpName.toLowerCase());
    }

    public void loadWarpLocation(){
        for(String key : warpConfiguration.getKeys(false)){
            warps.put(key, warpConfiguration.getLocation(key));
        }
    }

    public void saveWarpLocations(){
        for (String key : warpConfiguration.getKeys(false)) {
            warpConfiguration.set(key, null);
        }

        for(Map.Entry<String, Location> entry : warps.entrySet()){
            warpConfiguration.set(entry.getKey(), entry.getValue());
        }

        locationConfig.save();
    }

    public Set<String> getWarpNames(){
        return warps.keySet();
    }

}
