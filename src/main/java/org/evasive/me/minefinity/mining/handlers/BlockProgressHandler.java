package org.evasive.me.minefinity.mining.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.itembuilder.data.PickaxeData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.resolvers.PickaxeResolver;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;

import java.util.Objects;
import java.util.UUID;

public class BlockProgressHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final MiningDataMap miningDataMap;
    private final MiningAbilityRunner miningAbilityRunner;
    private final PickaxeResolver pickaxeResolver;
    BlockTierService blockTierService;
    BlockBreakHandler blockBreak;

    public BlockProgressHandler(PickaxeResolver pickaxeResolver, MiningAbilityRunner miningAbilityRunner, BlockTierService blockTierService, MilestoneService milestoneService, MiningDataMap miningDataMap, CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService) {
        this.blockTierService = blockTierService;
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

        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.isEmpty()) return;
        BasePickaxeItem basePickaxeItem = (BasePickaxeItem) customItemRegistryService.getRegisteredBaseItem(item);

        BaseBlock baseBlock = blockTierService.getSelectedBaseBlock(player);

        //Mining stats being used
        StatsContext statsContext = new StatsContext();

        miningAbilityRunner.runOnHit(basePickaxeItem, new HitContext(player, baseBlock, statsContext));

        statsContext.addSpeed(calculateMiningProgress(basePickaxeItem));

        float progress = statsContext.getSpeed();
        int health = baseBlock.health();

        if(progress > (float) health /MAX_SPEED_DENOMINATION)
            progress = (float) health /MAX_SPEED_DENOMINATION;

        createNewAnimation(player, progress, health, location.getBlock());

        miningDataMap.increaseBlockProgress(location, uuid, progress);

        if(miningDataMap.getBlockProgress(location, uuid) >= health){
            sendCleanPacket(player, location.getBlock());
            miningAbilityRunner.runOnBreak(basePickaxeItem, new BreakContext(player, baseBlock));
            blockBreak.handleBlockBreak(location, player);
        }

    }

    public float calculateMiningProgress(BasePickaxeItem basePickaxeItem){
        PickaxeData pickaxeData = pickaxeResolver.resolve(basePickaxeItem);
        return basePickaxeItem.calculateMiningSpeed(pickaxeData.getPickaxeParts());
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

        //if(!MinevolutionCore.miningMap.containsBlockLocation(block.getLocation()) || !MinevolutionCore.miningMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))return;

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
