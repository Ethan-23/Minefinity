package org.evasive.me.minefinity.core.service;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.Minefinity;

import java.util.HashMap;
import java.util.Map;

public class WarpService {

    Map<String, Location> warps = new HashMap<>();;
    private final ConfigurationSection warpConfiguration;

    public WarpService() {
        warpConfiguration = Minefinity.getCore().getConfig().getConfigurationSection("warps");

        if(warpConfiguration == null){
            Minefinity.getCore().getConfig().createSection("warps");
            Minefinity.getCore().saveConfig();
        }else{
            loadWarpLocation();
        }
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
        warpConfiguration.set("warps", null);
        for(Map.Entry<String, Location> entry : warps.entrySet()){
            warpConfiguration.set(entry.getKey(), entry.getValue());
        }
        Minefinity.getCore().saveConfig();
    }

}
