package org.evasive.me.minevolutionCore.worldPackets;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChunkLoadingEvents implements Listener {

    @EventHandler
    public void onPlayerChunkLoad(PlayerChunkLoadEvent event) {
        new MiningBlockHandler().replaceBlockPacketsInRegion(event.getPlayer(), event.getWorld());
    }

}
