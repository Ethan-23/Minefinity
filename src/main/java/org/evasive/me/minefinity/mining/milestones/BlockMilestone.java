package org.evasive.me.minefinity.mining.milestones;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;

import org.evasive.me.minefinity.core.data.ResourceData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlockMilestone implements PlayerDataComponent {

    private final Map<String, ResourceData> blockMilestoneMap;

    public BlockMilestone() {
        this.blockMilestoneMap = new HashMap<>();
    }

    public BlockMilestone(Map<String, ResourceData> blockMilestoneMap) {
        this.blockMilestoneMap = blockMilestoneMap;
    }

    public ResourceData getResourceData(String blockId) {
        if(!blockMilestoneMap.containsKey(blockId))
            blockMilestoneMap.put(blockId, new ResourceData(0, 0, 0));
        return blockMilestoneMap.get(blockId);
    }

    public void setBlocksMined(String blockId, int amount) {
        getResourceData(blockId).setBlockMined(amount);
    }

    public void setTier(String blockId, int tier) {
        getResourceData(blockId).setTier(tier);
    }

    public void setProgress(String blockId, int amount) {
        getResourceData(blockId).setProgress(amount);
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

    private void setResourceData(String blockId, ResourceData resourceData) {
        blockMilestoneMap.put(blockId, resourceData);
    }

    public Map<String, ResourceData> getMilestoneMap() {
        return Collections.unmodifiableMap(blockMilestoneMap);
    }

}
