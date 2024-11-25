package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class LapisOre implements BlockBuilder{
    @Override
    public String getName() {
        return "Lapis Ore";
    }

    @Override
    public Material getMaterial() {
        return Material.LAPIS_ORE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.LAPIS_ORE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.LAPIS_LAZULI;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
