package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class CoalBlock implements BlockBuilder{
    @Override
    public String getName() {
        return "Coal Block";
    }

    @Override
    public Material getMaterial() {
        return Material.COAL_BLOCK;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.COAL_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.COAL_BLOCK;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
