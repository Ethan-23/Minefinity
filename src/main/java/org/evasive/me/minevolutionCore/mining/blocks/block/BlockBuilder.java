package org.evasive.me.minevolutionCore.mining.blocks.block;

import org.bukkit.Material;

public interface BlockBuilder {
    int getTier();
    String getName();
    Material getMaterial();
    int getHealth();
    BlockType getBlockType();
    Material getBlockDrop();
    int getExperienceDrop();
}
