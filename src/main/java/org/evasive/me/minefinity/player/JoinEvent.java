package org.evasive.me.minefinity.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.Minefinity;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Minefinity.playerManager.registerPlayer(e.getPlayer());
    }

}
