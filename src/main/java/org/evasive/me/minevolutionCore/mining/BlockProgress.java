package org.evasive.me.minevolutionCore.mining;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.resourceblock.block.BlockDataFunctions;
import org.evasive.me.minevolutionCore.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minevolutionCore.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minevolutionCore.customItems.ItemFunctions;

import java.util.UUID;

public class BlockProgress {

    BlockBreak blockBreak = new BlockBreak();

    final public static int MAX_SPEED_DENOMINATION = 4;
    final int ANIMATION_STAGES = 10;

    public void addBlockProgress(Location location, Player player){
        UUID uuid = player.getUniqueId();
        float progress = calculateMiningProgress(uuid);

        int health = BlockDataFunctions.getBlockHealth(player);

        if(progress > (float) health /MAX_SPEED_DENOMINATION)
            progress = (float) health /MAX_SPEED_DENOMINATION;

        createNewAnimation(player, progress, health, location.getBlock());

        MinevolutionCore.miningMap.increaseBlockProgress(location, uuid, progress);



        if(MinevolutionCore.miningMap.getBlockProgress(location, uuid) >= health){
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

        float currentProgress = MinevolutionCore.miningMap.getBlockProgress(block.getLocation(), player.getUniqueId());
        int animUpdate = health / ANIMATION_STAGES;

        if((int)(currentProgress + progress) / animUpdate > (int)currentProgress / animUpdate || currentProgress == 0)
            sendAnimationPacket(player, block, (byte) ((currentProgress + progress) / animUpdate));

    }

    public void sendAnimationPacket(Player player, Block block, byte blockProgress){
        if(blockProgress > ANIMATION_STAGES - 1) blockProgress = ANIMATION_STAGES - 1;

        WrapperPlayServerBlockBreakAnimation progressAnimation = new WrapperPlayServerBlockBreakAnimation(
                MinevolutionCore.miningMap.getBlockAnimationID(block.getLocation(), player.getUniqueId()),
                new Vector3i(block.getX(), block.getY(), block.getZ()),
                blockProgress
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, progressAnimation);
    }

    public void sendCleanPacket(Player player, Block block){

        //if(!MinevolutionCore.miningMap.containsBlockLocation(block.getLocation()) || !MinevolutionCore.miningMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))return;

        WrapperPlayServerBlockBreakAnimation progressAnimation = new WrapperPlayServerBlockBreakAnimation(
                MinevolutionCore.miningMap.getBlockAnimationID(block.getLocation(), player.getUniqueId()),
                new Vector3i(block.getX(), block.getY(), block.getZ()),
                (byte) -1
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, progressAnimation);
    }

    public boolean isBlockStillValid(Location location){
        return MinevolutionCore.miningMap.containsBlockLocation(location);
    }
}
