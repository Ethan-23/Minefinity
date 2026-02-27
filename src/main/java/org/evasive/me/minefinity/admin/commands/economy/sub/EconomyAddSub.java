package org.evasive.me.minefinity.admin.commands.economy.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.SubCommand;
import org.evasive.me.minefinity.economy.EconomyService;
import org.evasive.me.minefinity.utils.CommandFeedback;

import static org.evasive.me.minefinity.utils.economy.EconNumberUtils.*;

public class EconomyAddSub implements SubCommand {

    EconomyService economyService = Minefinity.getCore().getEconomyService();

    @Override
    public String getName() {
        return "addEconomy";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        String playerName = args[1];
        Player target = Bukkit.getPlayerExact(playerName);

        double amount = Double.parseDouble(args[2]);

        economyService.addBalance(target, amount);

        assert target != null;
        sender.sendMessage(CommandFeedback.ADD_PLAYERS_BALANCE(target, amount));

    }
}
