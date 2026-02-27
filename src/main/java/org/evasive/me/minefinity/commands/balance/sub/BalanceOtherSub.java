package org.evasive.me.minefinity.commands.balance.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.SubCommand;
import org.evasive.me.minefinity.economy.EconomyService;
import org.evasive.me.minefinity.utils.CommandFeedback;

public class BalanceOtherSub implements SubCommand {

    EconomyService economyService = Minefinity.getCore().getEconomyService();

    @Override
    public String getName() {
        return "other";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) return;

        String playerName = args[0];
        Player target = Bukkit.getPlayerExact(playerName);

        if (target == null || !economyService.hasBalance(target)) {
            sender.sendMessage(CommandFeedback.INVALID_PLAYER);
            return;
        }

        double balance = economyService.getBalance(target);
        sender.sendMessage(CommandFeedback.GET_PLAYER_BALANCE(target, balance));
    }
}
