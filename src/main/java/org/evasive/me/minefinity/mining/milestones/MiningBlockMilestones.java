package org.evasive.me.minefinity.mining.milestones;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;

import org.evasive.me.minefinity.core.data.MiningResourceData;

import java.util.HashMap;
import java.util.Map;

public class MiningBlockMilestones implements PlayerDataComponent {

    private final Map<String, MiningResourceData> blockMilestoneMap;

    public MiningBlockMilestones() {
        this.blockMilestoneMap = new HashMap<>();
    }

    private MiningResourceData getResourceData(String blockId) {
        if(!blockMilestoneMap.containsKey(blockId))
            blockMilestoneMap.put(blockId, new MiningResourceData(0, 0, 0));
        return blockMilestoneMap.get(blockId);
    }

    public int getBlocksMined(String blockId) {
        return getResourceData(blockId).getBlockMined();
    }

    public int getTier(String blockId) {
        return getResourceData(blockId).getTier();
    }

    public int getProgress(String blockId) {
        return getResourceData(blockId).getProgress();
    }

    public void changeProgress(String blockId, int amount) {
        if(getProgress(blockId) + amount < 0)
            return;
        getResourceData(blockId).increaseProgress(amount);
    }

    public void changeTier(String blockId, int amount) {
        if(getTier(blockId) + amount < 0)
            return;
        getResourceData(blockId).increaseTier(amount);
    }

    public void changeBlocksMined(String blockId, int amount) {
        if(getBlocksMined(blockId) + amount < 0)
            return;
        getResourceData(blockId).increaseBlocksMined(amount);
    }

}
