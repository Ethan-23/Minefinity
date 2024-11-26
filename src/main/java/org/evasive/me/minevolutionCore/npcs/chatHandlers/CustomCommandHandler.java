package org.evasive.me.minevolutionCore.npcs.chatHandlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.npcs.InteractEvent;
import org.jetbrains.annotations.NotNull;

public class CustomCommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return false; // Command sent by a non-player
        }

        InteractEvent interactEvent = new InteractEvent();

        // Check which command was executed
        if (s.equalsIgnoreCase("enchant_action_yes")) {
            interactEvent.handleYes(player);
        } else if (s.equalsIgnoreCase("enchant_action_no")) {
            interactEvent.handleNo(player);
        }
        return true;
    }
}
