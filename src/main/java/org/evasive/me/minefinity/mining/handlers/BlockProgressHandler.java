package org.evasive.me.minefinity.mining.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.mining.utils.MiningProgress;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;

import java.util.UUID;

import static org.evasive.me.minefinity.mining.utils.MiningProgress.ANIMATION_STAGES;
import static org.evasive.me.minefinity.mining.utils.MiningProgress.crossedStage;

public class BlockProgressHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final StatsService statsService;
    private final MiningDataMap miningDataMap;
    private final MiningAbilityRunner miningAbilityRunner;
    private final BlockTierService blockTierService;
    private final BlockBreakHandler blockBreak;

    public BlockProgressHandler(MiningAbilityRunner miningAbilityRunner,
                                BlockTierService blockTierService, MiningDataMap miningDataMap,
                                CustomItemRegistryService customItemRegistryService, StatsService statsService,
                                BlockBreakHandler blockBreak) {
        this.miningAbilityRunner = miningAbilityRunner;
        this.blockTierService = blockTierService;
        this.miningDataMap = miningDataMap;
        this.customItemRegistryService = customItemRegistryService;
        this.statsService = statsService;
        this.blockBreak = blockBreak;
    }



    public void addBlockProgress(Location location, Player player){
        UUID uuid = player.getUniqueId();

        BaseBlock baseBlock = blockTierService.getSelectedBaseBlock(player);

        int playerBreakingPower = statsService.getStats(uuid).getOrDefault(Stats.BREAKING_POWER, 0);

        if(!MiningProgress.canBreak(playerBreakingPower, baseBlock.breakingPower()))
            return;

        StatsContext statsContext = new StatsContext();

        statsContext.addStats(statsService.getStringIdStats(player.getUniqueId()));

        BasePickaxeItem basePickaxeItem = getItemInHand(player);

        if (basePickaxeItem != null){
            miningAbilityRunner.runOnHit(basePickaxeItem, new HitContext(player, baseBlock, statsContext));
        }

        int health = baseBlock.health();

        float progress = MiningProgress.hitProgress(statsContext.getSpeed(), health);

        createNewAnimation(player, progress, health, location.getBlock());

        miningDataMap.increaseBlockProgress(location, uuid, progress);

        if(miningDataMap.getBlockProgress(location, uuid) >= health){
            sendCleanPacket(player, location.getBlock());
            blockBreak.handleBlockBreak(location, player, baseBlock, basePickaxeItem, statsContext, miningAbilityRunner);
        }

    }

    private BasePickaxeItem getItemInHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        BaseCustomItem baseCustomItem = customItemRegistryService.getRegisteredBaseItem(item);

        if(baseCustomItem instanceof BasePickaxeItem)
            return (BasePickaxeItem) baseCustomItem;

        return null;
    }

    private void createNewAnimation(Player player, float incomingProgress, int totalBlockHealth, Block block) {

        if(!isBlockStillValid(block.getLocation()))
            return;

        float currentProgress = miningDataMap.getBlockProgress(block.getLocation(), player.getUniqueId());

        int animationChange = MiningProgress.stageStep(totalBlockHealth);

        int totalProgress = (int) (currentProgress + incomingProgress);

        if(crossedStage(currentProgress, totalProgress, animationChange))
            sendAnimationPacket(player, block, (byte) (totalProgress / animationChange));

    }


    public void sendAnimationPacket(Player player, Block block, byte blockProgress){

        blockProgress = (byte) Math.min(blockProgress, ANIMATION_STAGES - 1);

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
