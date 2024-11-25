package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class PolishedGranite implements BlockBuilder{
    @Override
    public String getName() {
        return "Polished Granite";
    }

    @Override
    public Material getMaterial() {
        return Material.POLISHED_GRANITE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.POLISHED_GRANITE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.POLISHED_GRANITE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
