package org.evasive.me.minefinity.mining.scoreboard.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.mining.scoreboard.ScoreboardService;

public class ScoreboardConnectListener implements Listener {

    private final ScoreboardService scoreboardService;

    public ScoreboardConnectListener(ScoreboardService scoreboardService) {
        this.scoreboardService = scoreboardService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        scoreboardService.setupMainScoreboard(player);
    }

}
