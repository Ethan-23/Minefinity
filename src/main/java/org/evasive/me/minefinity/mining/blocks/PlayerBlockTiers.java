package org.evasive.me.minefinity.mining.blocks;

import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;

import java.util.HashMap;
import java.util.Map;

public class PlayerBlockTiers implements PlayerDataComponent {

    private final Map<String, Integer> unlockedBlockTiers = new HashMap<>();
    private final Map<String, String> selectedBlockTiers = new HashMap<>();

    public void setUnlockedBlockTier(String worldName, int tier){
        this.unlockedBlockTiers.put(worldName, tier);
    }

    public int getUnlockedBlockTier(String worldName){
        if(!this.unlockedBlockTiers.containsKey(worldName)){
            return unlockedBlockTiers.get("world");
        }
        return this.unlockedBlockTiers.get(worldName);
    }

    public void setSelectedBlockTier(String worldName, String tierName){
        this.selectedBlockTiers.put(worldName, tierName);
    }

    public String getSelectedBlockTier(String worldName){
        if(!this.selectedBlockTiers.containsKey(worldName)){
            throw new IllegalArgumentException("Invalid world name: " + worldName);
        }
        return this.selectedBlockTiers.get(worldName);
    }

    public boolean hasWorldUnlocked(String worldName){
        return selectedBlockTiers.containsKey(worldName);
    }
}
