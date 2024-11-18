package org.evasive.me.minevolutionCore.npcs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.npcs.npc.BlockMaster;

public class NPCJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        MinevolutionCore.getPlayerManager().registerPlayer(player);

        NPCManager npcManager = MinevolutionCore.getNpcManager();
        npcManager.getNpcTracker().addPlayer(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(MinevolutionCore.getCore(), bukkitTask -> new BlockMaster().prepareBlockMaster(player), 20);
    }
}
