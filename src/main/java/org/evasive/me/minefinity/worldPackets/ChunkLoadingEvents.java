package org.evasive.me.minefinity.worldPackets;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.player.sevices.TownService;

public class ChunkLoadingEvents implements Listener {

    TownService townService = Minefinity.getCore().getTownService();
    BlockTierService blockTierService = Minefinity.getCore().getBlockTierService();

    @EventHandler
    public void onPlayerChunkLoad(PlayerChunkLoadEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getChunk();

        if(hasRegion(player, chunk, blockTierService.MAIN_BLOCK_REGION))
            blockTierService.handleMainBlock(player);
        if(hasRegion(player, chunk, townService.TOWNHALL_REGION))
            townService.handleTownhallArea(player);
        if(hasRegion(player, chunk, townService.MERCHANT_REGION))
            townService.handleMerchantArea(player);
    }

    private boolean hasRegion(Player player, Chunk chunk, String regionName) {
        World bukkitWorld = player.getWorld();
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(bukkitWorld));

        if(regionManager == null || regionManager.getRegions() == null || regionManager.getRegion(regionName) == null) return false;

        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int maxX = minX + 15;
        int maxZ = minZ + 15;

        ProtectedRegion region = regionManager.getRegion(regionName);

        if(region == null) return false;

        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        if(min == null || max == null) return false;

        return min.x() <= maxX && max.x() >= minX &&
                min.z() <= maxZ && max.z() >= minZ;
    }


}
