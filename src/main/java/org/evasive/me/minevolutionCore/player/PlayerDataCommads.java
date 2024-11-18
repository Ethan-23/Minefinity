package org.evasive.me.minevolutionCore.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.jetbrains.annotations.NotNull;

import java.time.chrono.MinguoEra;

public class PlayerDataCommads implements CommandExecutor {

    public PlayerDataCommads(){
        MinevolutionCore.getCore().getCommand("minedata").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 3){
            if(strings[0].equals("tier")){
                Player player = Bukkit.getPlayer(strings[1]);
                MinevolutionCore.getPlayerManager().setBlockTier(player, Integer.parseInt(strings[2]));
                return true;
                // /minedata tier {playername} {tier#}
            }


        }

        return true;
    }
}
