package org.evasive.me.minefinity.mining.context;

import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;

import java.util.HashMap;
import java.util.Map;

public class DropContext {

    BlockType blockType;
    Map<CustomItem, Integer> dropMap = new HashMap<>();

    public DropContext(BlockType blockType) {
        this.blockType = blockType;
        //Base Drop
        dropMap.put(blockType.getBlock().blockDrop(), 1);
    }
}
