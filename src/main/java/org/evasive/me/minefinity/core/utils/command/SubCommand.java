package org.evasive.me.minefinity.core.utils.command;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    String getName();
    void execute(CommandSender sender, String[] args);
}
