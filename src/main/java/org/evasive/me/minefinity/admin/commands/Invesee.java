package org.evasive.me.minefinity.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Invesee implements CommandExecutor {

    public Invesee() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("invesee")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        if(args.length != 1){
            player.sendMessage(TextConversions.parse("<red>Useage /invesee <player>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){
            player.sendMessage(TextConversions.parse("<red>player not found"));
            return true;
        }

        player.openInventory(target.getInventory());
        player.sendMessage("Opening " + target.getName() + "'s Inventory");

        return true;
    }
}
