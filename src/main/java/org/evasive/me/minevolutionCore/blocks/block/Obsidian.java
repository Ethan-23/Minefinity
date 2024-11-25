package org.evasive.me.minevolutionCore.blocks.block;

import org.bukkit.Material;

public class Obsidian implements BlockBuilder {
    @Override
    public String getName() {
        return "Obsidian";
    }

    @Override
    public Material getMaterial() {
        return Material.OBSIDIAN;
    }

    @Override
    public int getHealth() {
        return 1000;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.OBSIDIAN;
    }

    @Override
    public Material getBlockDrop() {
        return Material.OBSIDIAN;
    }

    @Override
    public int getExperienceDrop() {
        return 1;
    }
}
