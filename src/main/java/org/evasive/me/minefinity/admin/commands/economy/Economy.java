package org.evasive.me.minefinity.admin.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.admin.commands.economy.sub.EconomyAddSub;
import org.evasive.me.minefinity.admin.commands.economy.sub.EconomySetSub;
import org.evasive.me.minefinity.admin.commands.economy.sub.EconomySubSub;
import org.evasive.me.minefinity.core.SubCommand;
import org.evasive.me.minefinity.utils.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Economy implements CommandExecutor {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public Economy() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("economy")).setExecutor(this);

        register(new EconomySetSub());
        register(new EconomyAddSub());
        register(new EconomySubSub());

    }

    private void register(SubCommand sub) {
        subCommands.put(sub.getName().toLowerCase(), sub);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 3) {
            sender.sendMessage(CommandFeedback.INVALID_ARGUMENTS);
            return true;
        }

        String type = args[0];
        String playerName = args[1];

        Player target = Bukkit.getPlayerExact(playerName);
        if(target == null) {
            sender.sendMessage(CommandFeedback.INVALID_PLAYER);
            return true;
        }

        int amount;

        try{
            amount = Integer.parseInt(args[2]);
        }catch (NumberFormatException e){
            sender.sendMessage(CommandFeedback.INVALID_AMOUNT);
            return true;
        }

        if(amount < 0){
            sender.sendMessage(CommandFeedback.UNDER_ZERO);
            return true;
        }

        if(type.equalsIgnoreCase("set"))
            subCommands.get("seteconomy").execute(sender, args);
        else if(type.equalsIgnoreCase("add"))
            subCommands.get("addeconomy").execute(sender, args);
        else if(type.equalsIgnoreCase("sub"))
            subCommands.get("subeconomy").execute(sender, args);

        return true;
    }
}
