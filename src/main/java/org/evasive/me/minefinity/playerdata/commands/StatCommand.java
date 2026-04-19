package org.evasive.me.minefinity.playerdata.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.playerdata.stats.gui.StatsInventory;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class StatCommand implements CommandExecutor {

    private final StatsService statsService;

    public StatCommand(StatsService statsService) {
        this.statsService = statsService;
        Objects.requireNonNull(Minefinity.getCore().getCommand("stats")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player))
            return true;

        new StatsInventory(player, statsService).open();

        return true;
    }
}
