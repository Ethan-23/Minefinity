package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class IronOre implements BlockBuilder{

    @Override
    public String getName() {
        return "Iron Ore";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_ORE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.IRON_ORE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.RAW_IRON;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
