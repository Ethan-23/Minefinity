package org.evasive.me.minefinity.playerdata.economy.commands.balance.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.economy.EconomyService;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.evasive.me.minefinity.core.utils.command.SubCommand;

public class BalanceOtherSub implements SubCommand {

    private final EconomyService economyService;

    public BalanceOtherSub(EconomyService economyService) {
        this.economyService = economyService;
    }

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
