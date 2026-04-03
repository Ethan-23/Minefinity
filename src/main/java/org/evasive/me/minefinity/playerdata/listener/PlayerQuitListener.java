package org.evasive.me.minefinity.playerdata.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.evasive.me.minefinity.playerdata.ranks.service.PermissionService;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.service.RankService;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final PlayerDataService playerDataService;
    private final RankService playerRankService;
    private final PermissionService permissionService;

    public PlayerQuitListener(PlayerDataService playerDataService, RankService playerRankService, PermissionService permissionService) {
        this.playerDataService = playerDataService;
        this.playerRankService = playerRankService;
        this.permissionService = permissionService;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();
        playerDataService.savePlayer(playerDataService.getPlayerData(uuid), true);

        permissionService.removePermissions(player);
        playerRankService.unloadRanks(uuid);

    }

}
