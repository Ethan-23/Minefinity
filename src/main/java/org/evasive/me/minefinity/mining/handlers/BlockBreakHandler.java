package org.evasive.me.minefinity.mining.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.CustomItemStack;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneService;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.mining.utils.MiningDrops;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final ItemPickupService itemPickupService;
    private final MiningMilestoneService milestoneService;
    private final BlockTierService blockTierService;
    private final MiningDataMap miningDataMap;
    private final BlockBreakNotifier blockBreakNotifier;

    public BlockBreakHandler(CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService, MiningMilestoneService milestoneService, BlockTierService blockTierService, MiningDataMap miningDataMap, BlockBreakNotifier blockBreakNotifier) {
        this.customItemRegistryService = customItemRegistryService;
        this.itemPickupService = itemPickupService;
        this.milestoneService = milestoneService;
        this.blockTierService = blockTierService;
        this.miningDataMap = miningDataMap;
        this.blockBreakNotifier = blockBreakNotifier;
    }

    public void handleBlockBreak(Location location, Player player, BaseBlock baseBlock, BasePickaxeItem basePickaxeItem, StatsContext statsContext, MiningAbilityRunner miningAbilityRunner) {

        BreakContext breakContext = new BreakContext(player, baseBlock, statsContext);

        List<CustomItemStack> drops = new ArrayList<>();

        boolean isSpecial = MiningDrops.addBlockDrops(drops, baseBlock, statsContext.getFortune(), statsContext.getSpecialChance());

        statsContext.setSpecialDrop(isSpecial);

        if(basePickaxeItem != null)
            miningAbilityRunner.runOnBreak(basePickaxeItem, breakContext);

        handlePlayerData(player, location);

        if(drops.isEmpty())
            return;

        int dropCount = 0;

        for(CustomItemStack customItemStack : drops){
            dropCount += itemPickupService.givePlayerDrops(player, customItemStack);
        }

        BaseCustomItem baseCustomItem = customItemRegistryService.getBaseItemById(drops.getFirst().getCustomItem());

        blockBreakNotifier.blockBreak(player, baseBlock.material(), drops.getFirst(), baseCustomItem.getRarity(), isSpecial, dropCount > 0);

    }

    private void handleMilestone(Player player, String blockId, int amount){
        milestoneService.increaseTierProgress(player, blockId, amount);
        milestoneService.increaseBlocksMined(player, blockId, amount);
    }

    private void handlePlayerData(Player player, Location location){
        String blockId = blockTierService.getSelectedMiningBlock(player, player.getWorld().getName());
        int blocksMined = 1;
        handleMilestone(player, blockId, blocksMined);
        miningDataMap.removeBlockPos(location, player.getUniqueId());
    }



}
