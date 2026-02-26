package org.evasive.me.minefinity.smelter.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.smelter.service.SmelterService;

public class SmelterEvents implements Listener {

    SmelterService smelterService = Minefinity.getCore().getSmelterService();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        smelterService.addOfflineProgress(player);
    }

}
