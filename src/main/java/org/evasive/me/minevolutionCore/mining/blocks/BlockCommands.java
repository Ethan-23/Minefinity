package org.evasive.me.minevolutionCore.mining.blocks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.mining.blocks.gui.BlockGUI;
import org.jetbrains.annotations.NotNull;

public class BlockCommands implements CommandExecutor {
    BlockGUI blockGUI = new BlockGUI();

    public BlockCommands(){
        MinevolutionCore.getCore().getCommand("blocks").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player))
            return true;
        Player player = (Player) commandSender;
        blockGUI.openInventory(player);
        return true;
    }
}
