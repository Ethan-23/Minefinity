package org.evasive.me.minefinity.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.evasive.me.minefinity.core.spawn.service.SpawnService;

public class ServerSpawnEvents implements Listener {

    private final SpawnService spawnService;

    public ServerSpawnEvents(SpawnService spawnService) {
        this.spawnService = spawnService;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        e.setRespawnLocation(spawnService.getSpawnLocation());
    }

}
