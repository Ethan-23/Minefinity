package org.evasive.me.minefinity.towns.worldPackets.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.towns.structures.data.Structure;
import org.evasive.me.minefinity.towns.structures.service.StructureService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private final StructureService structureService;
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    public PlayerMoveListener(StructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Location location = e.getFrom();

        if(getRegionAtLocation(location) == null)
            return;

        checkUnlockedRegion(player, e);

    }


    //Add a function that can be used anywhere
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

    private boolean isInsideRegion(Location location, String regionId){
        ProtectedRegion protectedRegion = getRegionAtLocation(location);
        if(protectedRegion == null)
            return false;
        return protectedRegion.getId().equals(regionId);
    }



    private void checkUnlockedRegion(Player player, PlayerMoveEvent e) {

        Location from = e.getFrom();
        Location to = e.getTo();

        ProtectedRegion region = getRegionAtLocation(to);

        if(region == null)
            return;

        String regionId = region.getId();

        String structureId;
        Structure structure;
        try{
            structureId = regionId.split("_")[1].toUpperCase();
            structure = Structure.valueOf(structureId);
        }catch(Exception exception){
            return;
        }

        int level = structureService.getStructureLevel(player, structure);

        if(level > 0 || player.getGameMode() == GameMode.CREATIVE)
            return;

        long now = System.currentTimeMillis();

        if(!cooldownMap.containsKey(player.getUniqueId()) || cooldownMap.get(player.getUniqueId()) < now){
            cooldownMap.put(player.getUniqueId(), now + 2000);
            player.sendMessage(TextConversions.parse("<red>You cannot enter this building yet!"));
        }

        if(isInsideRegion(to, regionId))
            e.setTo(from);

        if(!isInsideRegion(from, regionId))
            e.setTo(from);

        Vector movement = to.toVector().subtract(from.toVector());

        if (movement.lengthSquared() > 0) {
            movement.normalize();

            Vector push = movement.multiply(-0.8f);
            push.setY(0.2);

            Bukkit.getScheduler().runTask(Minefinity.getCore(), () -> {
                player.setVelocity(push);
            });
        }

    }

    /*

     */

}
