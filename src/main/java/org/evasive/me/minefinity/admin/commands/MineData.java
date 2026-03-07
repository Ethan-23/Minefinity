package org.evasive.me.minefinity.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.town.service.TownService;
import org.evasive.me.minefinity.town.Structure;
import org.evasive.me.minefinity.utils.TextConversions;
import org.evasive.me.minefinity.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

public class MineData implements CommandExecutor {

    private final BlockTierService blockTierService;
    private final TownService townService;

    public MineData(Minefinity minefinity, TownService townService, BlockTierService blockTierService) {
        this.townService = townService;
        this.blockTierService = blockTierService;
        minefinity.getCommand("minedata").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 3){

            if(strings[0].equals("tier")){
                Player player = Bukkit.getPlayer(strings[1]);
                blockTierService.setSelectedBlockTier(player, Integer.parseInt(strings[2]));
                return true;
            }

            String structureName = strings[0].toUpperCase();
            if(!Structure.contains(structureName)) {
                commandSender.sendMessage(CommandFeedback.INVALID_STRUCTURE);
                return true;
            }

            Player player = Bukkit.getPlayer(strings[1]);
            townService.setStructureLevel(player, Structure.valueOf(structureName), Integer.parseInt(strings[2]));
            return true;
        }

        return true;
    }
}
