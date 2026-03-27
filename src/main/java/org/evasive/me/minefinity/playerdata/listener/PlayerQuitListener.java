package org.evasive.me.minefinity.playerdata.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.service.RankService;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final PlayerDataService playerDataService;
    private final RankService playerRankService;

    public PlayerQuitListener(PlayerDataService playerDataService, RankService playerRankService) {
        this.playerDataService = playerDataService;
        this.playerRankService = playerRankService;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        playerDataService.savePlayer(playerDataService.getPlayerData(uuid), true);
        playerRankService.unloadRanks(uuid);
    }

}
