package org.evasive.me.minefinity.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.utils.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Vanish implements CommandExecutor, Listener {

    private final Set<UUID> vanished = new HashSet<>();
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getMainScoreboard();
    Team vanishTeam = board.getTeam("vanish");

    public Vanish() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("vanish")).setExecutor(this);

        if (vanishTeam == null) {
            vanishTeam = board.registerNewTeam("vanish");
            vanishTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) return true;

        if(!vanished.contains(player.getUniqueId())){
            hidePlayer(player);
        }else {
            showPlayer(player);
        }
        return true;
    }

    public void hidePlayer(Player player){
        vanished.add(player.getUniqueId());
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) {
                online.hidePlayer(Minefinity.getCore(), player);
            }
        }
        vanishTeam.addPlayer(player);
        player.sendMessage(CommandFeedback.VANISH_ON);
    }

    public void showPlayer(Player player){
        vanished.remove(player.getUniqueId());
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) {
                online.showPlayer(Minefinity.getCore(), player);
            }
        }
        vanishTeam.removePlayer(player);
        player.sendMessage(CommandFeedback.VANISH_OFF);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player joining = event.getPlayer();

        if(vanished.contains(joining.getUniqueId())){
            hidePlayer(joining);
            joining.sendMessage(CommandFeedback.VANISH_ON);
        }

        for (UUID vanishedUuid : vanished) {
            Player vanished = Bukkit.getPlayer(vanishedUuid);
            if (vanished != null) {
                joining.hidePlayer(Minefinity.getCore(), vanished);
            }
        }
    }

    public boolean isVanished(Player player){
        return vanished.contains(player.getUniqueId());
    }
}
