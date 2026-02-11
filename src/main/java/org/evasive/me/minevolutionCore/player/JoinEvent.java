package org.evasive.me.minevolutionCore.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minevolutionCore.MinevolutionCore;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        MinevolutionCore.playerManager.registerPlayer(e.getPlayer());
    }

}
