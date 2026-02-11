package org.evasive.me.minevolutionCore.resourceblock.block;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.resourceblock.BlockType;

public class BlockDataFunctions {

    /**
     * Returns the currently selected block tier of a player.
     * @param player The player whose selected blocks tier is being retrieved
     * @return The index of the player's selected block tier.
     */
    public static int getSelectedBlock(Player player){
        return MinevolutionCore.playerManager.getSelectedBlockTier(player);
    }

    /**
     * Returns the currently selected block material of a player.
     * @param player The player whose selected blocks material is being retrieved
     * @return The material of the player's selected block tier.
     */
    public static Material getBlockMaterial(Player player){
        return BlockType.values()[getSelectedBlock(player)].getBlock().material();
    }

    /**
     * Returns the currently selected block drop material of a player.
     * @param player The player whose selected blocks drop material is being retrieved
     * @return The drop material of the player's selected block tier.
     */
    public static String getMaterialDrop(Player player){
        return BlockType.values()[getSelectedBlock(player)].getBlock().blockDrop().getID();
    }

    /**
     * Returns the currently selected block health of a player.
     * @param player The player whose selected blocks health is being retrieved
     * @return The health of the player's selected block tier.
     */
    public static int getBlockHealth(Player player){
        return BlockType.values()[getSelectedBlock(player)].getBlock().health();
    }

    /**
     * Returns the currently selected blocks unlock cost of a player.
     * @param player The player whose selected blocks unlock cost is being retrieved
     * @return The unlock cost of the player's selected block tier.
     */
    public static int getBlockUnlockCost(Player player){
        return BlockType.values()[getSelectedBlock(player)].getBlock().unlockCost();
    }

}
