package org.evasive.me.minefinity.mining.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.notifications.NotificationService;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneService;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class BlockBreakHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final ItemPickupService itemPickupService;
    private final MiningMilestoneService milestoneService;
    private final BlockTierService blockTierService;
    private final MiningDataMap miningDataMap;
    private final BlockBreakNotifier blockBreakNotifier;

    private static final int SPECIAL_PERCENT_ROLL_MIN = 1;
    private static final int SPECIAL_PERCENT_ROLL_MAX = 101; // Exclusive Bound

    private static final int FORTUNE_PERCENT_ROLL_MIN = 1;
    private static final int FORTUNE_PERCENT_ROLL_MAX = 101; // Exclusive Bound

    public BlockBreakHandler(CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService, MiningMilestoneService milestoneService, BlockTierService blockTierService, MiningDataMap miningDataMap, NotificationService notificationService) {
        this.customItemRegistryService = customItemRegistryService;
        this.itemPickupService = itemPickupService;
        this.milestoneService = milestoneService;
        this.blockTierService = blockTierService;
        this.miningDataMap = miningDataMap;
        this.blockBreakNotifier = new BlockBreakNotifier(notificationService);
    }

    public void handleBlockBreak(Location location, Player player, BaseBlock baseBlock, BasePickaxeItem basePickaxeItem, StatsContext statsContext, MiningAbilityRunner miningAbilityRunner) {

        BreakContext breakContext = new BreakContext(player, baseBlock, statsContext);

        int amount = 1 + calculateFortuneDrops(statsContext.getFortune());

        handlePlayerData(player, location);

        statsContext.setSpecialDrop(baseBlock.specialBlockDropId() != null && ThreadLocalRandom.current().nextInt(SPECIAL_PERCENT_ROLL_MIN, SPECIAL_PERCENT_ROLL_MAX) <= statsContext.getSpecialChance());

        if(basePickaxeItem != null)
            miningAbilityRunner.runOnBreak(basePickaxeItem, breakContext);

        boolean specialDrop = statsContext.isSpecialDrop();

        ItemStack drop = specialDrop ? blockTierService.getSelectedSpecialDrop(player) : blockTierService.getSelectedBlockDrop(player);

        int drops = itemPickupService.givePlayerDrops(player, drop, amount);

        blockBreakNotifier.blockBreak(player, customItemRegistryService.getRegisteredBaseItem(drop), amount, specialDrop, drops > 0);

    }

    private void handleMilestone(Player player, String blockId){

        milestoneService.increaseTierProgress(player, blockId, 1);
    }

    private int calculateFortuneDrops(float fortuneStat){
        Random random = ThreadLocalRandom.current();

        int guaranteed = (int) fortuneStat / 100;
        int remainder = (int) (fortuneStat % 100);

        int randomNum = random.nextInt(FORTUNE_PERCENT_ROLL_MIN, FORTUNE_PERCENT_ROLL_MAX);

        return guaranteed + (randomNum <= remainder ? 1 : 0);
    }

    private void handlePlayerData(Player player, Location location){
        String blockId = blockTierService.getSelectedMiningBlock(player, player.getWorld().getName());
        milestoneService.increaseBlocksMined(player, blockId, 1);
        handleMilestone(player, blockId);
        miningDataMap.removeBlockPos(location, player.getUniqueId());
    }



}
