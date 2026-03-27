package org.evasive.me.minefinity.towns.worldPackets.events;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class PlayerMovePacketEvents extends PacketListenerAbstract {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION || event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION){

            Player player = event.getPlayer();
            checkForMiningRegion(player);
        }
    }

    public void checkForMiningRegion(Player player){
        if(player.getAttribute(Attribute.BLOCK_BREAK_SPEED) == null) {
            player.registerAttribute(Attribute.BLOCK_BREAK_SPEED);
        }
        //Makes it so if you enter the area of the main block break speed attr set to 0
        if(getRegionAtLocation(player.getLocation()) == null){
            player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(1);
        } else if(getRegionAtLocation(player.getLocation()).getId().equals("minefinity_town_center")){
            player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(0);
        }
    }

    public ProtectedRegion getRegionAtLocation(org.bukkit.Location location) {
        World world = location.getWorld();

        // Get the WorldGuardPlugin instance
        WorldGuardPlugin wgPlugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (wgPlugin == null) {
            return null; // WorldGuard is not installed or not enabled
        }

        // Get the WorldGuard RegionContainer
        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));

        if (regionManager == null) {
            return null; // No regions in this world
        }

        // Convert the Bukkit location to a BlockVector3 (WorldEdit's format)
        BlockVector3 locationVector = BlockVector3.at(location.getX(), location.getY(), location.getZ());

        // Get the ApplicableRegionSet (all regions that apply to the location)
        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(locationVector);

        // Return the first region found (if any)
        for (ProtectedRegion region : regionSet) {
            return region; // You can modify this to filter regions if needed
        }

        return null; // No region found
    }
}
