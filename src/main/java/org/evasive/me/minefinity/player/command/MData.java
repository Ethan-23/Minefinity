package org.evasive.me.minefinity.player.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.jetbrains.annotations.NotNull;

public class MData implements CommandExecutor {

    public MData(){
        Minefinity.getCore().getCommand("mdata").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 3){
            if(strings[0].equals("tier")){
                Player player = Bukkit.getPlayer(strings[1]);
                Minefinity.playerManager.setBlockTier(player, Integer.parseInt(strings[2]));
                return true;
                // /minedata tier {playername} {tier#}
            }


        }

        return true;
    }
}
