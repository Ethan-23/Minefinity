package org.evasive.me.minevolutionCore.mining.blocks.block;

import org.bukkit.Material;

public class Stone implements BlockBuilder {
    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public String getName() {
        return "Stone";
    }

    @Override
    public Material getMaterial() {
        return Material.STONE;
    }

    @Override
    public int getHealth() {
        return 80;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.STONE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.STONE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
