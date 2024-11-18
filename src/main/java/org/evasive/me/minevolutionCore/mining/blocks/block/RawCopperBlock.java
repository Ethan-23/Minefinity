package org.evasive.me.minevolutionCore.mining.blocks.block;

import org.bukkit.Material;

public class RawCopperBlock implements BlockBuilder{
    @Override
    public int getTier() {
        return 7;
    }

    @Override
    public String getName() {
        return "Raw Copper";
    }

    @Override
    public Material getMaterial() {
        return Material.RAW_COPPER_BLOCK;
    }

    @Override
    public int getHealth() {
        return 200;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.RAW_COPPER_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.RAW_COPPER;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
