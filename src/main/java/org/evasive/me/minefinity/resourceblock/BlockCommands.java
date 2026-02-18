package org.evasive.me.minefinity.resourceblock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.resourceblock.gui.BlockGui;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockCommands implements CommandExecutor {

    public BlockCommands(){
        Objects.requireNonNull(Minefinity.getCore().getCommand("blocks")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return true;
        new BlockGui(player).open();
        return true;
    }
}
