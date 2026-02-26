package org.evasive.me.minefinity.miner.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.miner.service.AutoMinerService;

public class AutoMinerEvents implements Listener {

    AutoMinerService autoMinerService = Minefinity.getCore().getAutoMinerService();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        autoMinerService.addOfflineProgress(player);
    }

}
