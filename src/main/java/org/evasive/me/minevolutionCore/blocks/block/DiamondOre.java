package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class DiamondOre implements BlockBuilder{
    @Override
    public String getName() {
        return "Diamond Ore";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_ORE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.DIAMOND_ORE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.DIAMOND;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
