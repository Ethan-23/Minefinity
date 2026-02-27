package org.evasive.me.minefinity.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.town.service.TownService;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

public class PacketRefresh implements CommandExecutor {

    private final static String SUCCESS_MESSAGE = "<yellow>Successfully refreshed packets for ";
    private final static String INVALID_PLAYER_MESSAGE = "<red>Cannot find player ";
    TownService townService =  Minefinity.getCore().getTownService();
    BlockTierService blockTierService =  Minefinity.getCore().getBlockTierService();


    public PacketRefresh() {
        Minefinity.getCore().getCommand("packetrefresh").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        Player target = args.length == 0 ? player :  Bukkit.getPlayer(args[0]);

        if(target == null){
            player.sendMessage(TextConversions.parse(INVALID_PLAYER_MESSAGE + args[0]));
            return true;
        }

        townService.handleMerchantArea(target);
        townService.handleTownhallArea(target);
        townService.handleForgeArea(target);
        townService.handleWorkshopArea(target);
        blockTierService.handleMainBlock(target);


        player.sendMessage(TextConversions.parse(SUCCESS_MESSAGE + (args.length == 0 ? player.getName() : args[0])));

        return true;
    }
}
