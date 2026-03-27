package org.evasive.me.minefinity.core.economy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.evasive.me.minefinity.core.utils.economy.EconNumberUtils.*;

public class Pay implements CommandExecutor {

    private final EconomyService economyService;

    public Pay(EconomyService economyService) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("pay")).setExecutor(this);
        this.economyService = economyService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length != 2) {
            player.sendMessage(TextConversions.parse("<white>/pay <name> <amount>"));
            return true;
        }

        String playerName = args[0];



        double amount;

        if(hasSuffix(args[1])) {
            amount = suffixToNumber(args[1]);
            if(amount < 0) {
                sender.sendMessage(CommandFeedback.INVALID_AMOUNT);
                return true;
            }
        } else {
            try{
                amount = round(Double.parseDouble(args[1]));
            }catch (NumberFormatException e){
                sender.sendMessage(CommandFeedback.INVALID_AMOUNT);
                return true;
            }
        }

        Player target = Bukkit.getPlayerExact(playerName);

        if (target == null || !economyService.hasBalance(target)) {
            player.sendMessage(TextConversions.parse("<red>" + playerName + " is not a valid player"));
            return true;
        }

        if(target.getUniqueId() == player.getUniqueId()){
            player.sendMessage(TextConversions.parse("<red>You cannot pay yourself"));
            return true;
        }

        if(amount <= 0){
            player.sendMessage(TextConversions.parse("<red>amount must be positive"));
            return true;
        }

        double senderBalance = economyService.getBalance(player);

        if(senderBalance < amount){
            player.sendMessage(TextConversions.parse("<red>You do not have enough money"));
            return true;
        }

        economyService.subBalance(player, amount);
        economyService.addBalance(target, amount);

        player.sendMessage(TextConversions.parse("<yellow>You have payed " + target.getName() + " <green>$" + balanceSuffix(amount)));
        target.sendMessage(TextConversions.parse("<yellow>You received <green>$" + balanceSuffix(amount) + " <yellow>from " + player.getName()));

        return true;
    }
}
