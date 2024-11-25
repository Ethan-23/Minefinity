package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class Andesite implements BlockBuilder{
    @Override
    public String getName() {
        return "Andesite";
    }

    @Override
    public Material getMaterial() {
        return Material.ANDESITE;
    }

    @Override
    public int getHealth() {
        return 200;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.ANDESITE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.ANDESITE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
