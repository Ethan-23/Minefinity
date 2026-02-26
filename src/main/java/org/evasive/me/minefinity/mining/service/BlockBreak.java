package org.evasive.me.minefinity.mining.service;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.framework.CustomItemRegistry;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.player.sevices.MilestoneService;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.utils.TextConversions;

import static org.evasive.me.minefinity.Minefinity.miningMap;
import static org.evasive.me.minefinity.utils.TextConversions.buildRarityColor;


public class BlockBreak {

    private final MilestoneService milestoneService = Minefinity.getCore().getMilestoneService();
    private final BlockTierService blockTierService = Minefinity.getCore().getBlockTierService();

    public void handleBlockBreak(Location location, Player player){

        int amount = 1; // Change later based on fortune ect

        BlockType blockType = blockTierService.getSelectedBlockType(player);

        milestoneService.increaseBlocksMined(player, blockType, 1);
        playBreakSound(player);
        handleMilestone(player, blockType, amount);
        miningMap.removeBlockPos(location, player.getUniqueId());

        String dropId = blockTierService.getSelectedBlockDrop(player).getID();

        int overflow = Minefinity.itemGiver.givePlayerDrops(player, dropId, amount);

        if(overflow > 0) return;

        sendActionBar(player, dropId, amount);

    }

    private void sendActionBar(Player player, String dropId, int amount){
        player.sendActionBar(TextConversions.parse("<green>+" + amount + " " + buildRarityColor(dropId, CustomItemRegistry.getByID(dropId).getBuilder().getRarity())));
    }

    private void playBreakSound(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 0.3f);
    }

    private void handleMilestone(Player player, BlockType blockType, int amount){
        milestoneService.increaseTierProgress(player, blockType, amount);
    }



}
