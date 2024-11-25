package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class DiamondBlock implements BlockBuilder{
    @Override
    public String getName() {
        return "Diamond Block";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_BLOCK;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.DIAMOND_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.DIAMOND_BLOCK;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
