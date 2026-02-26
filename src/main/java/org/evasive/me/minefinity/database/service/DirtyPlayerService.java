package org.evasive.me.minefinity.database.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DirtyPlayerService {

    Set<UUID> dirtyPlayers = new HashSet<>();

    public void addDirtyPlayer(Player player) {
        if(dirtyPlayers.contains(player.getUniqueId())) return;
        Bukkit.getConsoleSender().sendMessage(player.getName() + " Added");
        dirtyPlayers.add(player.getUniqueId());
    }

    public void removeDirtyPlayer(Player player){
        dirtyPlayers.remove(player.getUniqueId());
    }

    public Set<UUID> getDirtyPlayers(){
        return Collections.unmodifiableSet(dirtyPlayers);
    }

    public void clearDirtyPlayers(){
        dirtyPlayers.clear();
    }

}
