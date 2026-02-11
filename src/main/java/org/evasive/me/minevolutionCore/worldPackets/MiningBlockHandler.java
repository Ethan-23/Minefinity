package org.evasive.me.minevolutionCore.worldPackets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.resourceblock.block.BlockDataFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MiningBlockHandler {

    List<Material> fakeBlocks = new ArrayList<>(Arrays.asList(Material.WARPED_STEM, Material.WARPED_FENCE, Material.WARPED_SLAB, Material.WARPED_PLANKS));
    final List<Material> blockset1 = new ArrayList<>(Arrays.asList(Material.COBBLESTONE, Material.COBBLESTONE_WALL, Material.COBBLESTONE_SLAB, Material.CHISELED_STONE_BRICKS));
    List<Material> blockset2 = new ArrayList<>(Arrays.asList(Material.STONE, Material.ANDESITE_WALL, Material.STONE_SLAB, Material.CHISELED_STONE_BRICKS));

    public void replaceBlockPacketsInRegion(Player player, World world) {
        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        ProtectedRegion region = regionManager.getRegion("minevolution");
        List<Location> spongeBlocks = new ArrayList<>();
        List<Location> warpedPlanksBlocks = new ArrayList<>();
        List<Location> warpedFenceBlocks = new ArrayList<>();
        List<Location> warpedSlabBlocks = new ArrayList<>();
        List<Location> warpedStemBlocks = new ArrayList<>();

        // Collect block locations in the region
        for (int x = region.getMinimumPoint().x(); x <= region.getMaximumPoint().x(); x++) {
            for (int y = region.getMinimumPoint().y(); y <= region.getMaximumPoint().y(); y++) {
                for (int z = region.getMinimumPoint().z(); z <= region.getMaximumPoint().z(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    Material blockMaterial = block.getType();
                    Location location = block.getLocation();

                    // Categorize blocks based on their material
                    switch (blockMaterial) {
                        case SPONGE -> spongeBlocks.add(location);
                        case WARPED_PLANKS -> warpedPlanksBlocks.add(location);
                        case WARPED_FENCE -> warpedFenceBlocks.add(location);
                        case WARPED_SLAB -> warpedSlabBlocks.add(location);
                        case WARPED_STEM -> warpedStemBlocks.add(location);
                    }
                }
            }
        }

        // Process each category of blocks in batches
        processBlocksInBatches(player, spongeBlocks, loc -> sendFakeBlockChange(player, loc, BlockDataFunctions.getBlockMaterial(player)));
        processBlocksInBatches(player, warpedPlanksBlocks, loc -> sendFakeBlockChange(player, loc, blockset1.getFirst()));
        processBlocksInBatches(player, warpedFenceBlocks, loc -> sendFakeBlockChange(player, loc, blockset1.get(1)));
        processBlocksInBatches(player, warpedSlabBlocks, loc -> sendFakeBlockChange(player, loc, blockset1.get(2)));
        processBlocksInBatches(player, warpedStemBlocks, loc -> sendFakeBlockChange(player, loc, blockset1.get(3)));
    }

    private void processBlocksInBatches(Player player, List<Location> blockLocations, java.util.function.Consumer<Location> action) {
        int batchSize = 100; // Number of blocks to process per tick
        int totalBatches = (int) Math.ceil((double) blockLocations.size() / batchSize);

        for (int i = 0; i < totalBatches; i++) {
            int startIndex = i * batchSize;
            int endIndex = Math.min(startIndex + batchSize, blockLocations.size());

            // Schedule a task to process the current batch
            Bukkit.getScheduler().runTaskLater(MinevolutionCore.getCore(), () -> {
                for (int j = startIndex; j < endIndex; j++) {
                    action.accept(blockLocations.get(j));
                }
            }, i); // Delay by `i` ticks to spread processing across multiple ticks
        }
    }

    public void sendFakeBlockChange(Player player, Location location, Material newMaterial) {
        WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange(new Vector3i(location.getBlockX(), location.getBlockY(),location.getBlockZ()), SpigotConversionUtil.fromBukkitBlockData(newMaterial.createBlockData()).getGlobalId());
        // Send the packet to the player
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

}
