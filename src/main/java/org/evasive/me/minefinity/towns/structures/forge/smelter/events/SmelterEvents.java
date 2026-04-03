package org.evasive.me.minefinity.towns.structures.forge.smelter.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.towns.structures.forge.smelter.service.SmelterService;

public class SmelterEvents implements Listener {

    private final SmelterService smelterService;

    public SmelterEvents(SmelterService smelterService) {
        this.smelterService = smelterService;
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        smelterService.addOfflineProgress(player);
    }

}
