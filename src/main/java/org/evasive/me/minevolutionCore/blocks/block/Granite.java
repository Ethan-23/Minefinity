package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class Granite implements BlockBuilder{
    @Override
    public String getName() {
        return "Granite";
    }

    @Override
    public Material getMaterial() {
        return Material.GRANITE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.GRANITE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.GRANITE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
