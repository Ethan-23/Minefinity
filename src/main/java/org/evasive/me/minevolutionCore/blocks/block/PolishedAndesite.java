package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class PolishedAndesite implements BlockBuilder{

    @Override
    public String getName() {
        return "Polished Andesite";
    }

    @Override
    public Material getMaterial() {
        return Material.POLISHED_ANDESITE;
    }

    @Override
    public int getHealth() {
        return 260;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.POLISHED_ANDESITE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.POLISHED_ANDESITE;
    }

    @Override
    public int getExperienceDrop() {
        return 0;
    }
}
