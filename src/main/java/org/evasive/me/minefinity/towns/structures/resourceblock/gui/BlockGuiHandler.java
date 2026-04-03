package org.evasive.me.minefinity.towns.structures.resourceblock.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.core.utils.TextConversions;


public class BlockGuiHandler {

    private final Component INVALID_PURCHASE_ORDER = TextConversions.parse("<red>You must purchase the blocks in order</red>");
    private final Component INVALID_PURCHASE_AMOUNT = TextConversions.parse("<red>You cannot afford to purchase this block tier</red>");
    private final Component VALID_PURCHASE_MESSAGE = TextConversions.parse("<white>You have purchased the next block tier</white>");

    private final MilestoneService milestoneService;
    private final BlockTierService blockTierService;
    private final EconomyService economyService;
    private final CustomItemRegistryService customItemRegistryService;

    public BlockGuiHandler(MilestoneService milestoneService, CustomItemRegistryService customItemRegistryService, BlockTierService blockTierService, EconomyService economyService) {
        this.milestoneService = milestoneService;
        this.blockTierService = blockTierService;
        this.economyService = economyService;
        this.customItemRegistryService = customItemRegistryService;
    }

    /**
     * Handles player clicking on a locked block
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public boolean lockedBlockClicked(Player player, int blockTier, String worldId) {

        if(blockTier != blockTierService.getUnlockedMiningBlock(player, worldId) + 1){
            player.sendMessage(INVALID_PURCHASE_ORDER);
            return false;
        }

        return attemptBlockPurchase(player, blockTier, worldId);
    }

    /**
     * Attempts to purchase the clicked block with the players balance resulting in a purchase or denial
     * @param player
     * @param blockTier blockTier tier index of the clicked block in the progression track
     */
    public boolean attemptBlockPurchase(Player player, int blockTier, String worldId){

        double cost = blockTierService.getBlockUnlockCost(player, worldId);

        if(economyService.getBalance(player) < cost){
            player.sendMessage(INVALID_PURCHASE_AMOUNT);
            return false;
        }

        economyService.subBalance(player, cost);

        player.sendMessage(VALID_PURCHASE_MESSAGE);

        blockTierService.setUnlockedMiningBlock(player, worldId, blockTier);
        return true;
    }

    /**
     * Changes the players selected block tier and reopens the gui
     * @param player
     * @param blockTier tier to set block
     */
    public void handleSelect(Player player, int blockTier, String worldId){
        blockTierService.setSelectedMiningBlock(player, worldId, blockTier);
    }

    /**
     * Opens the milestone gui for the selected material
     * @param player
     * @param blockTier String Id of block
     */
    public void handleMilestone(Player player, int blockTier, String worldId){
        new MilestoneGui(
                player,
                blockTierService.getBlockTypeRegistryService().getBlockIdByTier(worldId, blockTier),
                customItemRegistryService,
                blockTierService,
                milestoneService,
                economyService,
                worldId
        ).open();
    }
}
