package org.evasive.me.minefinity.mining.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    public void handleBlockBreak(Location location, Player player, BaseBlock baseBlock, BasePickaxeItem basePickaxeItem, StatsContext statsContext, MiningAbilityRunner miningAbilityRunner) {

        BreakContext breakContext = new BreakContext(player, baseBlock, statsContext);

        int amount = 1 + calculateFortuneDrops(statsContext.getFortune());

        handlePlayerData(player, location);

        statsContext.setSpecialDrop(baseBlock.specialBlockDropId() != null && new Random().nextInt(1, 101) <= statsContext.getSpecialChance());

        if(basePickaxeItem != null)
            miningAbilityRunner.runOnBreak(basePickaxeItem, breakContext);

        playBreakSound(player, statsContext.isSpecialDrop());

        ItemStack drop = statsContext.isSpecialDrop() ? blockTierService.getSelectedSpecialDrop(player) : blockTierService.getSelectedBlockDrop(player);
        if(itemPickupService.givePlayerDrops(player, drop, amount) == 0)
            sendActionBar(player, statsContext.isSpecialDrop() ? baseBlock.specialBlockDropId() : baseBlock.blockDropId(), amount);

    }

    private void sendActionBar(Player player, String dropId, int amount){
        player.sendActionBar(TextConversions.parse("<green>+" + amount + " " + buildRarityColor(dropId, customItemRegistryService.getBaseItemById(dropId).getRarity())));
    }

    private void playBreakSound(Player player, boolean specialDrop){
        Sound sound = specialDrop ? Sound.BLOCK_SPAWNER_BREAK : Sound.BLOCK_GILDED_BLACKSTONE_BREAK;
        player.playSound(player.getLocation(), sound, 0.3f, 0.3f);
    }

    private void handleMilestone(Player player, String blockId){

        milestoneService.increaseTierProgress(player, blockId, 1);
    }

    private int calculateFortuneDrops(float fortuneStat){
        Random random = ThreadLocalRandom.current();

        int guaranteed = (int) fortuneStat / 100;
        int remainder = (int) (fortuneStat % 100);

        int randomNum = random.nextInt(1, 101);

        return guaranteed + (randomNum <= remainder ? 1 : 0);
    }

    private void handlePlayerData(Player player, Location location){
        String blockId = blockTierService.getSelectedMiningBlock(player, player.getWorld().getName());
        milestoneService.increaseBlocksMined(player, blockId, 1);
        handleMilestone(player, blockId);
        miningDataMap.removeBlockPos(location, player.getUniqueId());
    }



}
