package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class RawGoldBlock implements BlockBuilder{
    @Override
    public String getName() {
        return "Raw Gold Block";
    }

    @Override
    public Material getMaterial() {
        return Material.RAW_GOLD_BLOCK;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.RAW_GOLD_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.RAW_GOLD;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
