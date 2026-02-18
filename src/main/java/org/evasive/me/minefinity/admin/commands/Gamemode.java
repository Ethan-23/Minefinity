package org.evasive.me.minefinity.admin.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Gamemode implements CommandExecutor {

    public Gamemode(){
        Objects.requireNonNull(Minefinity.getCore().getCommand("gamemode")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))return true;
        if(args.length != 1){
            player.sendMessage(TextConversions.parse("<red>Invalid Usage of command"));
            return true;
        }

        String gamemode = args[0];

        if(List.of("creative", "1", "c").contains(gamemode.toLowerCase())){
            player.sendMessage("Game mode has been set to creative");
            player.setGameMode(GameMode.CREATIVE);
            return true;
        }else if(List.of("survival", "0", "s").contains(gamemode.toLowerCase())){
            player.sendMessage("Game mode has been set to survival");
            player.setGameMode(GameMode.SURVIVAL);
            return true;
        }else if(List.of("adventure", "2", "a").contains(gamemode.toLowerCase())){
            player.sendMessage("Game mode has been set to adventure");
            player.setGameMode(GameMode.ADVENTURE);
            return true;
        }else if(List.of("spectator", "3", "sp").contains(gamemode.toLowerCase())){
            player.sendMessage("Game mode has been set to spectator");
            player.setGameMode(GameMode.SPECTATOR);
            return true;
        }else{
            player.sendMessage(TextConversions.parse("<red>Invalid Usage of command"));
            return true;
        }
    }
}
