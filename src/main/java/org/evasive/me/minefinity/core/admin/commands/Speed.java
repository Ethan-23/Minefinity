package org.evasive.me.minefinity.core.admin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Speed implements CommandExecutor {

    public Speed() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("speed")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) return true;

        if(args.length != 1){
            player.sendMessage(CommandFeedback.INVALID_USAGE);
            return true;
        }

        int speed;

        try{
            speed = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            player.sendMessage(CommandFeedback.INVALID_NUMBER);
            return true;
        }

        if(speed < 1 || speed > 10){
            player.sendMessage(CommandFeedback.INVALID_NUMBER);
            return true;
        }

        boolean flying = player.isFlying();
        float playerSpeed = speed / 10f;

        if(flying){
            player.sendMessage("Flying speed set to " + speed);
            player.setFlySpeed(playerSpeed);
        }else {
            player.sendMessage("Walking speed set to " + speed);
            player.setWalkSpeed(playerSpeed);
        }

        return true;
    }
}
