package org.evasive.me.minefinity.mining.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.resolvers.PickaxeResolver;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;

import java.util.Map;
import java.util.UUID;

public class BlockProgressHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final StatsService statsService;
    private final MiningDataMap miningDataMap;
    private final MiningAbilityRunner miningAbilityRunner;
    private final PickaxeResolver pickaxeResolver;
    BlockTierService blockTierService;
    BlockBreakHandler blockBreak;

    public BlockProgressHandler(PickaxeResolver pickaxeResolver, MiningAbilityRunner miningAbilityRunner, BlockTierService blockTierService, MilestoneService milestoneService, MiningDataMap miningDataMap, CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService, StatsService statsService) {
        this.blockTierService = blockTierService;
        this.statsService = statsService;
        this.miningDataMap = miningDataMap;
        this.pickaxeResolver = pickaxeResolver;
        this.miningAbilityRunner = miningAbilityRunner;
        this.customItemRegistryService = customItemRegistryService;
        this.blockBreak = new BlockBreakHandler(customItemRegistryService, itemPickupService, milestoneService, blockTierService, miningDataMap);
    }

    final public static int MAX_SPEED_DENOMINATION = 4;
    final int ANIMATION_STAGES = 10;

    public void addBlockProgress(Location location, Player player){
        UUID uuid = player.getUniqueId();

        Map<Stats, Integer> playerStats = statsService.getStats(uuid);

        int breakingPower = playerStats.get(Stats.BREAKING_POWER);

        ItemStack item = player.getInventory().getItemInMainHand();

        BasePickaxeItem basePickaxeItem = null;

        if(!item.isEmpty())
            basePickaxeItem = (BasePickaxeItem) customItemRegistryService.getRegisteredBaseItem(item);

        BaseBlock baseBlock = blockTierService.getSelectedBaseBlock(player);

        if(baseBlock.breakingPower() > breakingPower)
            return;

        //Mining stats being used
        StatsContext statsContext = new StatsContext();

        statsContext.addStats(statsService.getStringIdStats(player.getUniqueId()));

        if (basePickaxeItem != null){
            miningAbilityRunner.runOnHit(basePickaxeItem, new HitContext(player, baseBlock, statsContext));
        }

        float progress = statsContext.getSpeed() / 10f; // divide by 10 to allow for bigger numbers for better display

        int health = baseBlock.health();

        if(progress > (float) health /MAX_SPEED_DENOMINATION)
            progress = (float) health /MAX_SPEED_DENOMINATION;

        createNewAnimation(player, progress, health, location.getBlock());

        miningDataMap.increaseBlockProgress(location, uuid, progress);

        if(miningDataMap.getBlockProgress(location, uuid) >= health){
            sendCleanPacket(player, location.getBlock());
            blockBreak.handleBlockBreak(location, player, baseBlock, basePickaxeItem, statsContext, miningAbilityRunner);
        }

    }

    private void createNewAnimation(Player player, float progress, int health, Block block) {

        if(!isBlockStillValid(block.getLocation()))
            return;

        float currentProgress = miningDataMap.getBlockProgress(block.getLocation(), player.getUniqueId());
        int animUpdate = health / ANIMATION_STAGES;

        if((int)(currentProgress + progress) / animUpdate > (int)currentProgress / animUpdate || currentProgress == 0)
            sendAnimationPacket(player, block, (byte) ((currentProgress + progress) / animUpdate));

    }

    public void sendAnimationPacket(Player player, Block block, byte blockProgress){
        if(blockProgress > ANIMATION_STAGES - 1) blockProgress = ANIMATION_STAGES - 1;

        WrapperPlayServerBlockBreakAnimation progressAnimation = new WrapperPlayServerBlockBreakAnimation(
                miningDataMap.getBlockAnimationID(block.getLocation(), player.getUniqueId()),
                new Vector3i(block.getX(), block.getY(), block.getZ()),
                blockProgress
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, progressAnimation);
    }

    public void sendCleanPacket(Player player, Block block){

        WrapperPlayServerBlockBreakAnimation progressAnimation = new WrapperPlayServerBlockBreakAnimation(
                miningDataMap.getBlockAnimationID(block.getLocation(), player.getUniqueId()),
                new Vector3i(block.getX(), block.getY(), block.getZ()),
                (byte) -1
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, progressAnimation);
    }

    public boolean isBlockStillValid(Location location){
        return miningDataMap.containsBlockLocation(location);
    }
}
