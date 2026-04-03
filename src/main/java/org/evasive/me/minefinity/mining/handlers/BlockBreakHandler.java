package org.evasive.me.minefinity.mining.handlers;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;

import java.util.Random;

import static org.evasive.me.minefinity.core.utils.TextConversions.buildRarityColor;


public class BlockBreakHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final ItemPickupService itemPickupService;
    private final MilestoneService milestoneService;
    private final BlockTierService blockTierService;
    private final MiningDataMap miningDataMap;

    public BlockBreakHandler(CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService, MilestoneService milestoneService, BlockTierService blockTierService, MiningDataMap miningDataMap) {
        this.customItemRegistryService = customItemRegistryService;
        this.itemPickupService = itemPickupService;
        this.milestoneService = milestoneService;
        this.blockTierService = blockTierService;
        this.miningDataMap = miningDataMap;
    }

    public void handleBlockBreak(Location location, Player player, StatsContext statsContext) {

        float fortune = statsContext.getFortune();

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int randomNum = random.nextInt(1, 101);

        int fortuneAmount = (int)(((int) fortune/100f) + (randomNum <= fortune % 100 ? 1 : 0));

        int amount = 1 + fortuneAmount;

        String blockId = blockTierService.getSelectedMiningBlock(player, player.getWorld().getName());
        BaseBlock baseBlock = blockTierService.getSelectedBaseBlock(player);

        milestoneService.increaseBlocksMined(player, blockId, 1);
        playBreakSound(player);
        handleMilestone(player, blockId, amount);
        miningDataMap.removeBlockPos(location, player.getUniqueId());

        ItemStack drop = blockTierService.getSelectedBlockDrop(player);

        int overflow = itemPickupService.attemptBackpackStorage(player, drop, amount);

        if(overflow > 0)
            overflow = itemPickupService.attemptInventoryStorage(player, drop, amount);

        if(overflow > 0){
            itemPickupService.fullInventoryNotification(player);
            return;
        }


        sendActionBar(player, baseBlock.blockDropId(), amount);

    }

    private void sendActionBar(Player player, String dropId, int amount){
        player.sendActionBar(TextConversions.parse("<green>+" + amount + " " + buildRarityColor(dropId, customItemRegistryService.getBaseItemById(dropId).getRarity())));
    }

    private void playBreakSound(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 0.3f);
    }

    private void handleMilestone(Player player, String blockId, int amount){
        milestoneService.increaseTierProgress(player, blockId, amount);
    }



}
