package org.evasive.me.minefinity.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.evasive.me.minefinity.core.admin.service.VanishService;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.core.scoreboard.ScoreboardService;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTypeRegistryService;

public class ServerJoinEvent implements Listener {

    private final VanishService vanishService;
    private final ScoreboardService scoreboard;

    private static final String PLAYER_JOIN = "<gray>[<#555555>+<gray>] <#55FFFF>";
    private static final String PLAYER_LEAVE = "<gray>[<#555555>-<gray>] <#55FFFF>";

    public ServerJoinEvent(VanishService vanishService, ScoreboardService scoreboard) {
        this.vanishService = vanishService;
        this.scoreboard = scoreboard;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        event.joinMessage(vanishService.isVanished(player) ? null : TextConversions.parse(PLAYER_JOIN + player.getName()));
        scoreboard.setupMainScoreboard(player);

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.quitMessage(vanishService.isVanished(player) ? null : TextConversions.parse(PLAYER_LEAVE + player.getName()));
    }
}
