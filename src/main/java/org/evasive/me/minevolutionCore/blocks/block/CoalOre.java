package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class CoalOre implements BlockBuilder {
    @Override
    public String getName() {
        return "Coal Ore";
    }
    @Override
    public Material getMaterial() {
        return Material.COAL_ORE;
    }

    @Override
    public int getHealth() {
        return 100;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.COAL_ORE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.COAL;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
