package org.evasive.me.minevolutionCore.customItems.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.customItems.CustomItemRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Mgive implements CommandExecutor {

    public Mgive(){
        Objects.requireNonNull(MinevolutionCore.getCore().getCommand("mgive")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(strings.length == 0){
            commandSender.sendMessage(CustomItemRegistry.getAllItemIDs().toString());
            return true;
        }
        if(!(commandSender instanceof Player player))
            return true;

        if(strings.length == 1){
            String itemName = strings[0].toUpperCase();
            giveItem(player, itemName);
            return true;

        }else if(strings.length == 2 && Bukkit.getPlayer(strings[0]) != null){
            Player givenPlayer = Bukkit.getPlayer(strings[0]);
            String itemName = strings[1].toUpperCase();
            giveItem(givenPlayer, itemName);
            return true;
        }
        return true;
    }



    public void giveItem(Player player, String itemName){
        if(!CustomItemRegistry.isRegistered(itemName)) {
            player.sendMessage(itemName + " not found.");
            return;
        }
        player.getInventory().addItem(CustomItemRegistry.getByID(itemName).getBuilder().buildItem());
    }
}
