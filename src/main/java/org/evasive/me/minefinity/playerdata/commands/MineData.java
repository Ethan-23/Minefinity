package org.evasive.me.minefinity.playerdata.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.towns.structures.service.StructureService;
import org.evasive.me.minefinity.towns.structures.data.Structure;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MineData implements CommandExecutor {

    private final BlockTierService blockTierService;
    private final StructureService townService;

    public MineData(Minefinity minefinity, StructureService townService, BlockTierService blockTierService) {
        this.townService = townService;
        this.blockTierService = blockTierService;
        Objects.requireNonNull(minefinity.getCommand("minedata")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(strings.length == 3){

            if(strings[0].equals("tier")){
                Player player = Bukkit.getPlayer(strings[1]);
                if(player == null){
                    commandSender.sendMessage(CommandFeedback.INVALID_PLAYER);
                    return true;
                }
                blockTierService.setSelectedMiningBlock(player, player.getWorld().getName(), Integer.parseInt(strings[2]));
                return true;
            }

            String structureName = strings[0].toUpperCase();
            if(!Structure.contains(structureName)) {
                commandSender.sendMessage(CommandFeedback.INVALID_STRUCTURE);
                return true;
            }

            Player player = Bukkit.getPlayer(strings[1]);

            if(player == null){
                commandSender.sendMessage(CommandFeedback.INVALID_PLAYER);
                return true;
            }

            townService.setStructureLevel(player, Structure.valueOf(structureName), Integer.parseInt(strings[2]));
            return true;
        }

        return true;
    }
}
