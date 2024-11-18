package org.evasive.me.minevolutionCore.worldPackets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.mining.blocks.BlockDataFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MiningBlockHandler{

    BlockDataFunctions blockDataFunctions = new BlockDataFunctions();
    List<Material> fakeBlocks = new ArrayList<>(Arrays.asList(Material.WARPED_STEM, Material.WARPED_FENCE, Material.WARPED_SLAB, Material.WARPED_PLANKS));
    List<Material> blockset1 = new ArrayList<>(Arrays.asList(Material.COBBLESTONE, Material.COBBLESTONE_WALL, Material.COBBLESTONE_SLAB, Material.CHISELED_STONE_BRICKS));
    List<Material> blockset2 = new ArrayList<>(Arrays.asList(Material.STONE, Material.ANDESITE_WALL, Material.STONE_SLAB, Material.CHISELED_STONE_BRICKS));

    public void replaceBlockPacketsInRegion(Player player){

        Location location = player.getLocation();
        ProtectedRegion region = getRegionAtLocation(location);

        if(region == null)
            return;
        if(!region.getId().equals("mainblock") && !region.getId().equals("minevolution"))
            return;

        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        for (int x = min.x(); x <= max.x(); x++) {
            for (int y = min.y(); y <= max.y(); y++) {
                for (int z = min.z(); z <= max.z(); z++) {
                    location = new Location(player.getWorld(), x, y, z);
                    Material blockMaterial = location.getBlock().getType();

                    // If block matches the target material, send a fake block change
                    if (blockMaterial != Material.SPONGE && !fakeBlocks.contains(blockMaterial))
                        continue;

                    if (blockMaterial == Material.SPONGE){
                        sendFakeBlockChange(player, location, new BlockDataFunctions().getBlockMaterial(player));
                    }else if(blockMaterial == Material.WARPED_PLANKS) {
                        sendFakeBlockChange(player, location, blockset1.getFirst());
                    }else if(blockMaterial == Material.WARPED_FENCE) {
                        sendFakeBlockChange(player, location, blockset1.get(1));
                    }else if(blockMaterial == Material.WARPED_SLAB) {
                        sendFakeBlockChange(player, location, blockset1.get(2));
                    }else if(blockMaterial == Material.WARPED_STEM) {
                        sendFakeBlockChange(player, location, blockset1.get(3));
                    }
                }
            }
        }
    }



    public static ProtectedRegion getRegionAtLocation(Location location) {
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



    public void sendFakeBlockChange(Player player, Location location, Material newMaterial) {
        WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange(new Vector3i(location.getBlockX(), location.getBlockY(),location.getBlockZ()), SpigotConversionUtil.fromBukkitBlockData(newMaterial.createBlockData()).getGlobalId());
        // Send the packet to the player
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

}
