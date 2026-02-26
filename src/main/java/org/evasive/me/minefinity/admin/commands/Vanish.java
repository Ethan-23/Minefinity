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
import org.evasive.me.minefinity.admin.service.VanishService;
import org.evasive.me.minefinity.utils.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Vanish implements CommandExecutor, Listener {

    VanishService vanishService = Minefinity.getCore().getVanishService();

    public Vanish() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("vanish")).setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) return true;

        if(!vanishService.isVanished(player)){
            vanishService.hide(player);
            player.sendMessage(CommandFeedback.VANISH_ON);
        }else {
            vanishService.show(player);
            player.sendMessage(CommandFeedback.VANISH_OFF);
        }
        return true;
    }

}
