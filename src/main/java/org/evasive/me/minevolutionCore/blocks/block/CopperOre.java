package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class CopperOre implements BlockBuilder {
    @Override
    public String getName() {
        return "Copper Ore";
    }

    @Override
    public Material getMaterial() {
        return Material.COPPER_ORE;
    }

    @Override
    public int getHealth() {
        return 160;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.COPPER_ORE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.RAW_COPPER;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
