package org.evasive.me.minefinity.playerdata.economy.commands.balance.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.economy.EconomyService;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.evasive.me.minefinity.core.utils.command.SubCommand;

public class BalanceSelfSub implements SubCommand {

    private final EconomyService economyService;

    public BalanceSelfSub(EconomyService economyService) {
        this.economyService = economyService;
    }

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
