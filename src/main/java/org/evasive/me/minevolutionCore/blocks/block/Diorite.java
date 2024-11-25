package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class Diorite implements BlockBuilder {
    @Override
    public String getName() {
        return "Diorite";
    }

    @Override
    public Material getMaterial() {
        return Material.DIORITE;
    }

    @Override
    public int getHealth() {
        return 120;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.DIORITE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.DIORITE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
