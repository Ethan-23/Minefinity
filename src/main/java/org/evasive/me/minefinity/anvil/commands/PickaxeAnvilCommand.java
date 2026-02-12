package org.evasive.me.minefinity.anvil.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.anvil.PickaxeAnvilGui;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PickaxeAnvilCommand implements CommandExecutor {

    public PickaxeAnvilCommand() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("pickaxeanvil")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player))
            return true;
        new PickaxeAnvilGui(player).open();
        return true;
    }
}
