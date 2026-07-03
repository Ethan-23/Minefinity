package org.evasive.me.minefinity.playerdata.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;

public class PlayerPreLoginListener implements Listener {

    private final PlayerDataService playerDataService;

    public PlayerPreLoginListener(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!playerDataService.loadPlayer(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    TextConversions.parse("Couldn't load your data. Please try again in a moment."));
        }
    }

}
