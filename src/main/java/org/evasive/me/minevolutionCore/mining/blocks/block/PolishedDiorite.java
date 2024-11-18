package org.evasive.me.minevolutionCore.mining.blocks.block;

import org.bukkit.Material;

public class PolishedDiorite implements BlockBuilder {
    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public String getName() {
        return "Polished Diorite";
    }

    @Override
    public Material getMaterial() {
        return Material.POLISHED_DIORITE;
    }

    @Override
    public int getHealth() {
        return 140;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.POLISHED_DIORITE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.POLISHED_DIORITE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
