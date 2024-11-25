package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class OakLog implements BlockBuilder{
    @Override
    public String getName() {
        return "Oak Log";
    }

    @Override
    public Material getMaterial() {
        return Material.OAK_LOG;
    }

    @Override
    public int getHealth() {
        return 40;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.OAK_LOG;
    }

    @Override
    public Material getBlockDrop() {
        return Material.OAK_PLANKS;
    }

    @Override
    public int getExperienceDrop() {
        return 0;
    }
}
