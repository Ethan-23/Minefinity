package org.evasive.me.minefinity.core.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.towns.structures.service.StructureService;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PacketRefresh implements CommandExecutor {

    private final static String SUCCESS_MESSAGE = "<yellow>Successfully refreshed packets for ";
    private final StructureService townService;
    private final BlockTierService blockTierService;


    public PacketRefresh(StructureService townService, BlockTierService blockTierService) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("packetrefresh")).setExecutor(this);
        this.townService = townService;
        this.blockTierService = blockTierService;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        Player target = args.length == 0 ? player :  Bukkit.getPlayer(args[0]);

        if(target == null){
            player.sendMessage(CommandFeedback.INVALID_PLAYER);
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
