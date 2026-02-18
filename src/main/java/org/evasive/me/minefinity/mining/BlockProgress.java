package org.evasive.me.minefinity.mining;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minefinity.customItems.ItemFunctions;
import org.evasive.me.minefinity.player.sevices.BlockTierService;

import java.util.UUID;

public class BlockProgress {

    BlockTierService blockTierService = Minefinity.core.getBlockTierService();
    BlockBreak blockBreak = new BlockBreak();

    final public static int MAX_SPEED_DENOMINATION = 4;
    final int ANIMATION_STAGES = 10;

    public void addBlockProgress(Location location, Player player){
        UUID uuid = player.getUniqueId();
        float progress = calculateMiningProgress(uuid);

        int health = blockTierService.getBlockTier(player).getBlock().health();

        if(progress > (float) health /MAX_SPEED_DENOMINATION)
            progress = (float) health /MAX_SPEED_DENOMINATION;

        createNewAnimation(player, progress, health, location.getBlock());

        Minefinity.miningMap.increaseBlockProgress(location, uuid, progress);



        if(Minefinity.miningMap.getBlockProgress(location, uuid) >= health){
            sendCleanPacket(player, location.getBlock());
            blockBreak.handleBlockBreak(location, player);
        }

    }

    public float calculateMiningProgress(UUID uuid){
        ItemStack item = Bukkit.getPlayer(uuid).getInventory().getItemInMainHand();
        BasePickaxeItem pickaxeItem = (BasePickaxeItem) PickaxeItem.valueOf(ItemFunctions.getItemId(item)).getBuilder();
        return pickaxeItem.getBaseMiningSpeed();
    }

    private void createNewAnimation(Player player, float progress, int health, Block block) {

        if(!isBlockStillValid(block.getLocation()))
            return;

        float currentProgress = Minefinity.miningMap.getBlockProgress(block.getLocation(), player.getUniqueId());
        int animUpdate = health / ANIMATION_STAGES;

        if((int)(currentProgress + progress) / animUpdate > (int)currentProgress / animUpdate || currentProgress == 0)
            sendAnimationPacket(player, block, (byte) ((currentProgress + progress) / animUpdate));

    }

    public void sendAnimationPacket(Player player, Block block, byte blockProgress){
        if(blockProgress > ANIMATION_STAGES - 1) blockProgress = ANIMATION_STAGES - 1;

        WrapperPlayServerBlockBreakAnimation progressAnimation = new WrapperPlayServerBlockBreakAnimation(
                Minefinity.miningMap.getBlockAnimationID(block.getLocation(), player.getUniqueId()),
                new Vector3i(block.getX(), block.getY(), block.getZ()),
                blockProgress
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, progressAnimation);
    }

    public void sendCleanPacket(Player player, Block block){

        //if(!MinevolutionCore.miningMap.containsBlockLocation(block.getLocation()) || !MinevolutionCore.miningMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))return;

        WrapperPlayServerBlockBreakAnimation progressAnimation = new WrapperPlayServerBlockBreakAnimation(
                Minefinity.miningMap.getBlockAnimationID(block.getLocation(), player.getUniqueId()),
                new Vector3i(block.getX(), block.getY(), block.getZ()),
                (byte) -1
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, progressAnimation);
    }

    public boolean isBlockStillValid(Location location){
        return Minefinity.miningMap.containsBlockLocation(location);
    }
}
