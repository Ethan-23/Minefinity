package org.evasive.me.minefinity.resourceblock.block.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.resourceblock.block.BlockDataFunctions;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.resourceblock.milestones.gui.MilestoneGui;
import org.evasive.me.minefinity.resourceblock.milestones.MilestoneFunctions;
import org.evasive.me.minefinity.utils.Messages;

import java.math.BigDecimal;

import static org.evasive.me.minefinity.Minefinity.playerManager;
import static org.evasive.me.minefinity.utils.EconUtils.getMoney;
import static org.evasive.me.minefinity.utils.EconUtils.subtractMoney;

public class BlockGuiHandler {

    private final Component INVALID_PURCHASE_ORDER = Messages.parse("<red>You must purchase the blocks in order</red>");
    private final Component INVALID_PURCHASE_AMOUNT = Messages.parse("<red>You cannot afford to purchase this block tier</red>");
    private final Component VALID_PURCHASE_MESSAGE = Messages.parse("<white>You have purchased the next block tier</white>");

    /**
     * Handles player clicking on a locked block
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public boolean lockedBlockClicked(Player player, int blockTier){

        if(blockTier != playerManager.getBlockTier(player) + 1){
            player.sendMessage(INVALID_PURCHASE_ORDER);
            return false;
        }

        return attemptBlockPurchase(player, blockTier);
    }

    /**
     * Attempts to purchase the clicked block with the players balance resulting in a purchase or denial
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public boolean attemptBlockPurchase(Player player, int blockTier){

        float cost = BlockDataFunctions.getBlockUnlockCost(player);

        if(getMoney(player.getUniqueId()).compareTo(BigDecimal.valueOf(cost)) < 0){
            player.sendMessage(INVALID_PURCHASE_AMOUNT);
            return false;
        }

        subtractMoney(player.getUniqueId(), cost);

        player.sendMessage(VALID_PURCHASE_MESSAGE);

        playerManager.setBlockTier(player, blockTier);
        handleSelect(player, blockTier);
        return true;
    }

    /**
     * Changes the players selected block tier and reopens the gui
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public void handleSelect(Player player, int blockTier){
        playerManager.setSelectedBlockTier(player, blockTier);
    }

    /**
     * Opens the milestone gui for the selected material
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public void handleMilestone(Player player, int blockTier){
        Material material = BlockType.values()[blockTier].getBlock().material();
        MilestoneFunctions.setSelectedMaterial(player, material);
        new MilestoneGui(player, material).open();
    }
}
