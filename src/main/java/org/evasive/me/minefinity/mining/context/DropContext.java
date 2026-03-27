package org.evasive.me.minefinity.mining.context;

import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;

import java.util.HashMap;
import java.util.Map;

public class DropContext {

    BaseBlock baseBlock;
    Map<CustomItem, Integer> dropMap = new HashMap<>();

    public DropContext(BaseBlock baseBlock) {
        this.baseBlock = baseBlock;
        //Base Drop
        //dropMap.put(blockType.getBlock().blockDropId(), 1);
    }
}
