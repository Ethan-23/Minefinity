package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class EmeraldBlock implements BlockBuilder{
    @Override
    public String getName() {
        return "Emerald Block";
    }

    @Override
    public Material getMaterial() {
        return Material.EMERALD_BLOCK;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.EMERALD_BLOCK;
    }

    @Override
    public Material getBlockDrop() {
        return Material.EMERALD_BLOCK;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
