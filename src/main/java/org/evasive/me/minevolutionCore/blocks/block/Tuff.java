package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class Tuff implements BlockBuilder{
    @Override
    public String getName() {
        return "Tuff";
    }

    @Override
    public Material getMaterial() {
        return Material.TUFF;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.TUFF;
    }

    @Override
    public Material getBlockDrop() {
        return Material.TUFF;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
