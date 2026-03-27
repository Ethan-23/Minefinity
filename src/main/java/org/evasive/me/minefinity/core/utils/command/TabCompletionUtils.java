package org.evasive.me.minefinity.core.utils.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TabCompletionUtils {

    public static List<String> getOnlinePlayers(String string){
        return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(string.toLowerCase()))
                .collect(Collectors.toList());
    }

}
