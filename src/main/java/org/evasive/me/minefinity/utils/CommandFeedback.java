package org.evasive.me.minefinity.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static org.evasive.me.minefinity.utils.economy.EconNumberUtils.balanceSuffix;
import static org.evasive.me.minefinity.utils.economy.EconNumberUtils.wholeBalanceDisplay;

public class CommandFeedback {

    public static Component INVALID_ARGUMENTS = TextConversions.parse("<red>Invalid Arguments!");
    public static Component INVALID_PLAYER = TextConversions.parse("<red>Invalid Player!");
    public static Component INVALID_AMOUNT = TextConversions.parse("<red>Invalid Amount!");
    public static Component UNDER_ZERO = TextConversions.parse("<red>Amount must be a positive number!");
    public static Component STAFFMODE_ON = TextConversions.parse("<yellow>Staff mode on");
    public static Component STAFFMODE_OFF = TextConversions.parse("<yellow>Staff mode off");
    public static Component VANISH_ON = TextConversions.parse("<blue>You are now vanished");
    public static Component VANISH_OFF = TextConversions.parse("<blue>You are now shown");

    public static Component GET_PLAYER_BALANCE(Player player, double balance){
        return TextConversions.parse("<yellow>" + player.getName() + "'s balance: <green>$" + wholeBalanceDisplay(balance));
    }

    public static Component ADD_PLAYERS_BALANCE(Player player, double amount){
        return TextConversions.parse("<green>$" + balanceSuffix(amount) + " <yellow>has been added to " + player.getName() + "'s account");
    }

    public static Component SUB_PLAYERS_BALANCE(Player player, double amount){
        return TextConversions.parse("<green>$" + balanceSuffix(amount) + " <yellow>has been removed from " + player.getName() + "'s account");
    }

    public static Component SET_PLAYERS_BALANCE(Player player, double amount){
        return TextConversions.parse("<yellow>" + player.getName() + "'s account has been set to <green>$" + wholeBalanceDisplay(amount));
    }

}
