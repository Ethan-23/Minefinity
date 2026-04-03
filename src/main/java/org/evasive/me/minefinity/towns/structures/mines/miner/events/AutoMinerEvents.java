package org.evasive.me.minefinity.towns.structures.mines.miner.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.towns.structures.mines.miner.service.AutoMinerService;

public class AutoMinerEvents implements Listener {

    private final AutoMinerService autoMinerService;

    public AutoMinerEvents(AutoMinerService autoMinerService) {
        this.autoMinerService = autoMinerService;
    }

    //Need to figure out why this is running before player data is being set
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        autoMinerService.addOfflineProgress(player);
    }

}
