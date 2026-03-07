package org.evasive.me.minefinity.mining.context;

import org.evasive.me.minefinity.resourceblock.framework.BlockType;

public class BreakContext {

    BlockType blockType;

    //Base Case
    public BreakContext(BlockType blockType) {
        this.blockType = blockType;
    }
}
