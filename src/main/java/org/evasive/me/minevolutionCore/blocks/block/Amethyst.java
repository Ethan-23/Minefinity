package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class Amethyst implements BlockBuilder{
    @Override
    public String getName() {
        return "Amethyst";
    }

    @Override
    public Material getMaterial() {
        return Material.AMETHYST_BLOCK;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.AMETHYST;
    }

    @Override
    public Material getBlockDrop() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
