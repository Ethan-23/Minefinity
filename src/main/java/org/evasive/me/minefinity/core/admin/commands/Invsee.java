package org.evasive.me.minefinity.core.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Invsee implements CommandExecutor {

    public Invsee() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("invsee")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        if(args.length != 1){
            player.sendMessage(CommandFeedback.INVALID_USAGE);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){
            player.sendMessage(CommandFeedback.INVALID_PLAYER);
            return true;
        }

        player.openInventory(target.getInventory());
        player.sendMessage(CommandFeedback.INVESEE_SUCCESS(target));

        return true;
    }
}
