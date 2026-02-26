package org.evasive.me.minefinity.player;

import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.resourceblock.service.ResourceData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlockMilestone {

    private final Map<BlockType, ResourceData> blockMilestoneMap;

    public BlockMilestone() {
        this.blockMilestoneMap = new HashMap<>();
        for(BlockType blockType : BlockType.values()){
            blockMilestoneMap.put(blockType, new ResourceData(1, 0, 0));
        }
    }

    public BlockMilestone(Map<BlockType, ResourceData> blockMilestoneMap) {
        this.blockMilestoneMap = blockMilestoneMap;
    }

    public ResourceData getResourceData(BlockType blockType) {
        return blockMilestoneMap.getOrDefault(blockType, null);
    }

    public void setBlocksMined(BlockType blockType, int amount) {
        getResourceData(blockType).setBlockMined(amount);
    }

    public void setTier(BlockType blockType, int tier) {
        getResourceData(blockType).setTier(tier);
    }

    public void setProgress(BlockType blockType, int amount) {
        getResourceData(blockType).setProgress(amount);
    }

    public int getBlocksMined(BlockType blockType) {
        return getResourceData(blockType).getBlockMined();
    }

    public int getTier(BlockType blockType) {
        return getResourceData(blockType).getTier();
    }

    public int getProgress(BlockType blockType) {
        return getResourceData(blockType).getProgress();
    }

    private void setResourceData(BlockType blockType, ResourceData resourceData) {
        blockMilestoneMap.put(blockType, resourceData);
    }

    public Map<BlockType, ResourceData> getMilestoneMap() {
        return Collections.unmodifiableMap(blockMilestoneMap);
    }

}
