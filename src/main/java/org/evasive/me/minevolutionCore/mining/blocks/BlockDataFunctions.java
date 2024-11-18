package org.evasive.me.minevolutionCore.mining.blocks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.mining.blocks.block.BlockType;
import org.evasive.me.minevolutionCore.player.PlayerManager;

public class BlockDataFunctions {

    public int getSelectedBlock(Player player){
        return MinevolutionCore.getPlayerManager().getSelectedBlockTier(player);
    }

    public Material getBlockMaterial(Player player){
        return BlockType.values()[getSelectedBlock(player)].getBlockCreator().getMaterial();
    }

    public Material getMaterialDrop(Player player){
        return BlockType.values()[getSelectedBlock(player)].getBlockCreator().getBlockDrop();
    }

    public int getBlockHealth(Player player){
        return BlockType.values()[getSelectedBlock(player)].getBlockCreator().getHealth();
    }

    public int getBlockExperience(Player player){
        return BlockType.values()[getSelectedBlock(player)].getBlockCreator().getExperienceDrop();
    }
}
