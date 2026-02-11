package org.evasive.me.minevolutionCore.utils;

import com.github.retrooper.packetevents.protocol.world.Location;

public class LocationConvert {

    public static Location convertLocation(org.bukkit.Location location){
        return new Location(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

}
