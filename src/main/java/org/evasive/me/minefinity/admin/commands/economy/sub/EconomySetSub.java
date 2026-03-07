package org.evasive.me.minefinity.admin.commands.economy.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.SubCommand;
import org.evasive.me.minefinity.economy.EconomyService;
import org.evasive.me.minefinity.utils.command.CommandFeedback;

public class EconomySetSub implements SubCommand {

    EconomyService economyService = Minefinity.getCore().getEconomyService();

    @Override
    public String getName() {
        return "setEconomy";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        String playerName = args[1];
        Player target = Bukkit.getPlayerExact(playerName);

        double amount = Double.parseDouble(args[2]);

        economyService.setBalance(target, amount);

        assert target != null;
        sender.sendMessage(CommandFeedback.SET_PLAYERS_BALANCE(target, amount));

    }
}
