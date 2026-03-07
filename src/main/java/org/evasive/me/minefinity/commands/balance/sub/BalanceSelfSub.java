package org.evasive.me.minefinity.commands.balance.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.SubCommand;
import org.evasive.me.minefinity.economy.EconomyService;
import org.evasive.me.minefinity.utils.command.CommandFeedback;

public class BalanceSelfSub implements SubCommand {

    EconomyService economyService = Minefinity.getCore().getEconomyService();

    @Override
    public String getName() {
        return "self";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player target)) return;
        double balance = economyService.getBalance(target);
        target.sendMessage(CommandFeedback.GET_PLAYER_BALANCE(target, balance));
    }
}
