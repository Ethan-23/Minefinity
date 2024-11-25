package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class IronBlock implements BlockBuilder {

    @Override
    public String getName() {
        return "Iron Block";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_BLOCK;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.IRON_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.IRON_INGOT;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
