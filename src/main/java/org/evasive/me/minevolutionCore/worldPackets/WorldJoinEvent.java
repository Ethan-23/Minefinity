package org.evasive.me.minevolutionCore.worldPackets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minevolutionCore.MinevolutionCore;

public class WorldJoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        Bukkit.getScheduler().runTaskLater(MinevolutionCore.getCore(), bukkitTask -> new MiningBlockHandler().replaceBlockPacketsInRegion(player), 20);
    }
}
