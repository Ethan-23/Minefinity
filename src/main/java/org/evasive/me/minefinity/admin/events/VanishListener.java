package org.evasive.me.minefinity.admin.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.admin.service.VanishService;

import java.util.UUID;

public class VanishListener implements Listener {

    VanishService vanishService = Minefinity.getCore().getVanishService();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        boolean vanished = vanishService.isVanished(player);

        if(vanished){
            vanishService.hide(player);
            player.sendMessage("You are vanished!");
        }

        for (UUID vanishedUuid : vanishService.getVanished()) {
            Player vanishedPlayer = Bukkit.getPlayer(vanishedUuid);
            if (vanishedPlayer != null) {
                player.hidePlayer(Minefinity.getCore(), vanishedPlayer);
            }
        }
    }

}
