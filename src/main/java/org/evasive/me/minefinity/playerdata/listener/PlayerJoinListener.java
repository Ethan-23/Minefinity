package org.evasive.me.minefinity.playerdata.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.playerdata.ranks.service.PermissionService;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.service.RankService;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final PlayerDataService playerDataService;
    private final RankService playerRankService;
    PermissionService permissionService;

    public PlayerJoinListener(PlayerDataService playerDataService, RankService playerRankService, PermissionService permissionService) {
        this.playerDataService = playerDataService;
        this.playerRankService = playerRankService;
        this.permissionService = permissionService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        playerDataService.loadPlayerAsync(player.getUniqueId(), firstJoin -> {

            if (firstJoin) {
                player.sendMessage("Welcome to the Minefinity!");
            }

        });
        playerRankService.loadRanks(uuid);
    }

}
