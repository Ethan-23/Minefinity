package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class Calcite implements BlockBuilder{
    @Override
    public String getName() {
        return "Calcite";
    }

    @Override
    public Material getMaterial() {
        return Material.CALCITE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.CALCITE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.CALCITE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
