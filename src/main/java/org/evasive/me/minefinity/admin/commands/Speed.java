package org.evasive.me.minefinity.admin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.utils.TextConversions;
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
            player.sendMessage(TextConversions.parse("<red>Usage: /speed <1-10>"));
            return true;
        }

        int speed;

        try{
            speed = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            player.sendMessage(TextConversions.parse("<red>Invalid number!"));
            return true;
        }

        if(speed < 1 || speed > 10){
            player.sendMessage(TextConversions.parse("<red>Invalid speed. Must be between 1 and 10!"));
            return true;
        }

        boolean flying = player.isFlying();
        float playerSpeed = speed / 10f;

        if(flying){
            player.sendMessage("Flying speed set to " + playerSpeed);
            player.setFlySpeed(speed);
        }else {
            player.sendMessage("Walking speed set to " + playerSpeed);
            player.setWalkSpeed(speed);
        }

        return true;
    }
}
