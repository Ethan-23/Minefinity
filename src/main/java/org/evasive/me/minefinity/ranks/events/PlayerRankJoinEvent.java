package org.evasive.me.minefinity.ranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.evasive.me.minefinity.ranks.PermissionService;
import org.evasive.me.minefinity.ranks.RankManager;

import java.util.UUID;

public class PlayerRankJoinEvent implements Listener {

    private final RankManager rankManager;
    private final PermissionService permissionService;

    public PlayerRankJoinEvent(RankManager rankManager, PermissionService permissionService) {
        this.rankManager = rankManager;
        this.permissionService = permissionService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        UUID uuid = e.getPlayer().getUniqueId();

        if(!rankManager.containsPlayer(uuid))
            rankManager.addPlayer(uuid);

        permissionService.applyPermissions(e.getPlayer(), rankManager.getRanks(uuid));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        permissionService.removePermissions(player);
    }

}
