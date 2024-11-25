package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public interface BlockBuilder {
    String getName();
    Material getMaterial();
    int getHealth();
    BlockType getBlockType();
    Material getBlockDrop();
    int getExperienceDrop();
}
