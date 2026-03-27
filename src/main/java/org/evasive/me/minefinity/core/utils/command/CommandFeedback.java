package org.evasive.me.minefinity.core.utils.command;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.TextConversions;

import static org.evasive.me.minefinity.core.utils.economy.EconNumberUtils.balanceSuffix;
import static org.evasive.me.minefinity.core.utils.economy.EconNumberUtils.wholeBalanceDisplay;

public class CommandFeedback {

    public static Component INVALID_USAGE = TextConversions.parse("<red>Invalid usage of command!");
    public static Component INVALID_NUMBER = TextConversions.parse("<red>Invalid Number!");
    public static Component INVALID_PLAYER = TextConversions.parse("<red>Invalid Player!");
    public static Component INVALID_AMOUNT = TextConversions.parse("<red>Invalid Amount!");
    public static Component INVALID_STRUCTURE = TextConversions.parse("<red>Invalid Structure!");
    public static Component INVALID_WARP = TextConversions.parse("<red>Invalid Warp!");
    public static Component INVALID_RANK = TextConversions.parse("<red>Invalid Rank!");
    public static Component INVALID_ITEM_ID = TextConversions.parse("<red>Invalid Item ID!");
    public static Component UNDER_ZERO = TextConversions.parse("<red>Amount must be a positive number!");

    public static Component STAFFMODE_ON = TextConversions.parse("<yellow>Staff mode on.");
    public static Component STAFFMODE_OFF = TextConversions.parse("<yellow>Staff mode off.");
    public static Component SET_SPAWN = TextConversions.parse("<yellow>World spawn set.");
    public static Component VANISH_ON = TextConversions.parse("<blue>You are now vanished.");
    public static Component VANISH_OFF = TextConversions.parse("<blue>You are now shown.");

    public static Component GET_PLAYER_BALANCE(Player player, double balance){
        return TextConversions.parse("<yellow>" + player.getName() + "'s balance: <green>$" + wholeBalanceDisplay(balance) + ".");
    }

    public static Component ADD_PLAYERS_BALANCE(Player player, double amount){
        return TextConversions.parse("<green>$" + balanceSuffix(amount) + " <yellow>has been added to " + player.getName() + "'s account.");
    }

    public static Component SUB_PLAYERS_BALANCE(Player player, double amount){
        return TextConversions.parse("<green>$" + balanceSuffix(amount) + " <yellow>has been removed from " + player.getName() + "'s account.");
    }

    public static Component SET_PLAYERS_BALANCE(Player player, double amount){
        return TextConversions.parse("<yellow>" + player.getName() + "'s account has been set to <green>$" + wholeBalanceDisplay(amount) +".");
    }

    public static Component SET_PLAYERS_GAMEMODE(GameMode gameMode){
        return TextConversions.parse("Game mode has been set to " + TextConversions.formatItemName(gameMode.name()) + ".");
    }

    public static Component INVESEE_SUCCESS(Player player){
        return TextConversions.parse("Opening " + player.getName() + "'s Inventory.");
    }

    public static Component WARP_SUCCESS(String warpName){
        return TextConversions.parse("<yellow>You have been warped to " + warpName + ".");
    }

    public static Component DELETE_WARP(String warpName){
        return TextConversions.parse("<yellow>" + warpName + " warp has been deleted.");
    }

    public static Component CUSTOMITEM_DELETE_SUCCESS(String itemId){
        return TextConversions.parse("<yellow>" + itemId + " has been deleted.");
    }

}
