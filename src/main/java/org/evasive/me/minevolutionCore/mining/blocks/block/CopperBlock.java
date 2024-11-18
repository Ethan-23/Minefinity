package org.evasive.me.minevolutionCore.mining.blocks.block;

import org.bukkit.Material;

public class CopperBlock implements BlockBuilder{
    @Override
    public int getTier() {
        return 8;
    }

    @Override
    public String getName() {
        return "Copper Block";
    }

    @Override
    public Material getMaterial() {
        return Material.COPPER_BLOCK;
    }

    @Override
    public int getHealth() {
        return 240;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.COPPER_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.COPPER_INGOT;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
