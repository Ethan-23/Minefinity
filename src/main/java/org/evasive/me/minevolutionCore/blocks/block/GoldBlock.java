package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class GoldBlock implements BlockBuilder{
    @Override
    public String getName() {
        return "Gold Block";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_BLOCK;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.GOLD_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.GOLD_INGOT;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
