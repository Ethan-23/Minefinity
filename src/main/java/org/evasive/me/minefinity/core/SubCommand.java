package org.evasive.me.minefinity.core;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface SubCommand {
    String getName();
    void execute(CommandSender sender, String[] args);
}
