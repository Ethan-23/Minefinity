package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class RawIronBlock implements BlockBuilder{

    @Override
    public String getName() {
        return "Raw Iron Block";
    }

    @Override
    public Material getMaterial() {
        return Material.RAW_IRON_BLOCK;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.RAW_IRON_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.RAW_IRON;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
