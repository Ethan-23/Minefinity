package org.evasive.me.minefinity.resourceblock.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.economy.EconomyService;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.utils.TextConversions;


public class BlockGuiHandler {

    private final Component INVALID_PURCHASE_ORDER = TextConversions.parse("<red>You must purchase the blocks in order</red>");
    private final Component INVALID_PURCHASE_AMOUNT = TextConversions.parse("<red>You cannot afford to purchase this block tier</red>");
    private final Component VALID_PURCHASE_MESSAGE = TextConversions.parse("<white>You have purchased the next block tier</white>");

    private final BlockTierService blockTierService = Minefinity.getCore().getBlockTierService();
    private final EconomyService economyService = Minefinity.getCore().getEconomyService();

    /**
     * Handles player clicking on a locked block
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public boolean lockedBlockClicked(Player player, int blockTier){

        if(blockTier != blockTierService.getBlockTier(player).ordinal() + 1){
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

        double cost = blockTierService.getBlockUnlockCost(player);

        if(economyService.getBalance(player) < cost){
            player.sendMessage(INVALID_PURCHASE_AMOUNT);
            return false;
        }

        economyService.subBalance(player, cost);

        player.sendMessage(VALID_PURCHASE_MESSAGE);

        blockTierService.setBlockTier(player, BlockType.values()[blockTier]);
        handleSelect(player, blockTier);
        return true;
    }

    /**
     * Changes the players selected block tier and reopens the gui
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public void handleSelect(Player player, int blockTier){
        blockTierService.setSelectedBlockTier(player, blockTier);
    }

    /**
     * Opens the milestone gui for the selected material
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public void handleMilestone(Player player, int blockTier){
        new MilestoneGui(player, blockTier).open();
    }
}
