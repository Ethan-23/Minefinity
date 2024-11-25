package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class Deepslate implements BlockBuilder{
    @Override
    public String getName() {
        return "Deepslate";
    }

    @Override
    public Material getMaterial() {
        return Material.DEEPSLATE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.DEEPSLATE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.DEEPSLATE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
