package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class GoldOre implements BlockBuilder{
    @Override
    public String getName() {
        return "Gold Ore";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_ORE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.GOLD_ORE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.RAW_GOLD;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
