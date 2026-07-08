package org.evasive.me.minefinity.mining.data;

import org.bukkit.block.Block;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SelectedBlockMap {

    private final ConcurrentHashMap<UUID, Block> selectedBlockMap = new ConcurrentHashMap<>();

    public void addSelectedBlock(UUID uuid, Block block){
        selectedBlockMap.put(uuid, block);
    }

    public Block getSelectedBlock(UUID uuid){
        return selectedBlockMap.get(uuid);
    }

    public boolean hasPlayer(UUID uuid){
        return selectedBlockMap.containsKey(uuid);
    }

    public void removePlayer(UUID uuid){
        selectedBlockMap.remove(uuid);
    }

}
