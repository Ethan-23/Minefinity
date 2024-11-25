package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class EmeraldOre implements BlockBuilder{
    @Override
    public String getName() {
        return "Emerald Ore";
    }

    @Override
    public Material getMaterial() {
        return Material.EMERALD_ORE;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.EMERALD_ORE;
    }

    @Override
    public Material getBlockDrop() {
        return Material.EMERALD;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
