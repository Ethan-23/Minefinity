package org.evasive.me.minevolutionCore.anvil.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.anvil.PickaxeAnvilGui;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PickaxeAnvilCommand implements CommandExecutor {

    public PickaxeAnvilCommand() {
        Objects.requireNonNull(MinevolutionCore.getCore().getCommand("pickaxeanvil")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player))
            return true;
        new PickaxeAnvilGui(player).open();
        return true;
    }
}
