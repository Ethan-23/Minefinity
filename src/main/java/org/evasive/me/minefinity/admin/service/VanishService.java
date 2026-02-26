package org.evasive.me.minefinity.admin.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.evasive.me.minefinity.Minefinity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishService {

    private final Set<UUID> vanished = new HashSet<>();
    private final Team vanishTeam;

    public VanishService() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        Team team = board.getTeam("vanish");

        if (team == null) {
            team = board.registerNewTeam("vanish");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        this.vanishTeam = team;
    }

    public void hide(Player player) {

        vanished.add(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) {
                online.hidePlayer(Minefinity.getCore(), player);
            }
        }

        vanishTeam.addEntry(player.getName());
        Minefinity.getCore().getScoreboard().setupMainScoreboard(player);
    }

    public void show(Player player) {

        vanished.remove(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) {
                online.showPlayer(Minefinity.getCore(), player);
            }
        }

        vanishTeam.removeEntry(player.getName());
        Minefinity.getCore().getScoreboard().setupMainScoreboard(player);
    }

    public boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public Set<UUID> getVanished() {
        return Collections.unmodifiableSet(vanished);
    }

}
