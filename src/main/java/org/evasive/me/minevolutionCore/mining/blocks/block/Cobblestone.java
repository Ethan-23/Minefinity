package org.evasive.me.minevolutionCore.mining.blocks.block;

import org.bukkit.Material;

public class Cobblestone implements BlockBuilder {
    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public String getName() {
        return "Cobblestone";
    }

    @Override
    public Material getMaterial() {
        return Material.COBBLESTONE;
    }

    @Override
    public int getHealth() {
        return 60;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.COBBLESTONE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.COBBLESTONE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
