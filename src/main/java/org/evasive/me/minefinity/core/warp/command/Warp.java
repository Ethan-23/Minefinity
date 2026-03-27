package org.evasive.me.minefinity.core.warp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.warp.service.WarpService;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Warp implements CommandExecutor {

    private final WarpService warpService;

    public Warp(WarpService warpService) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("warp")).setExecutor(this);
        this.warpService = warpService;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        if(args.length != 1){
            player.sendMessage(CommandFeedback.INVALID_USAGE);
            return true;
        }

        String warpName = args[0];

        if(!warpService.containsWarpLocation(warpName)){
            player.sendMessage(CommandFeedback.INVALID_WARP);
            return true;
        }

        player.sendMessage(CommandFeedback.WARP_SUCCESS(warpName));
        player.teleport(warpService.getWarpLocation(warpName));

        return true;
    }
}
