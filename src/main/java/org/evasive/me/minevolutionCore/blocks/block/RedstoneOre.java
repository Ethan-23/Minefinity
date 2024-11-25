package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class RedstoneOre implements BlockBuilder{
    @Override
    public String getName() {
        return "Redstone Ore";
    }

    @Override
    public Material getMaterial() {
        return Material.REDSTONE_ORE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.REDSTONE_ORE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.REDSTONE;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
