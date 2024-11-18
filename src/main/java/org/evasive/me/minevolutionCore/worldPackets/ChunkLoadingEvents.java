package org.evasive.me.minevolutionCore.worldPackets;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChunkLoadingEvents implements Listener {

    @EventHandler
    public void onPlayerChunkLoad(PlayerChunkLoadEvent event) {
        new MiningBlockHandler().replaceBlockPacketsInRegion(event.getPlayer());
    }

}
