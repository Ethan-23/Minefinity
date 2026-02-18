package org.evasive.me.minefinity.player.sevices;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.mining.BlockProgress;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.worldPackets.MassBlockPacketSender;

import java.util.Map;

import static org.evasive.me.minefinity.Minefinity.miningMap;
import static org.evasive.me.minefinity.Minefinity.selectedBlockMap;

public class BlockTierService {

    public final String MAIN_BLOCK_REGION = "minefinity_block";
    private final PlayerManager playerManager;
    private final BlockProgress blockProgress;
    private final MassBlockPacketSender massBlockPacketSender = new MassBlockPacketSender();

    public BlockTierService(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.blockProgress = new BlockProgress();
    }

    private BlockType getBlockType(Player player){
        return playerManager.get(player).getBlockTier();
    }

    public void setBlockTier(Player player, BlockType blockType) {
        playerManager.get(player).setBlockTier(blockType);
    }

    public BlockType getBlockTier(Player player) {
        return playerManager.get(player).getBlockTier();
    }

    public BlockType getSelectedBlockType(Player player) {
        return BlockType.values()[playerManager.get(player).getSelectedBlockTier()];
    }

    public int getSelectedBlockTier(Player player){
        return playerManager.get(player).getSelectedBlockTier();
    }

    public void setSelectedBlockTier(Player player, int tier){
        playerManager.get(player).setSelectedBlockTier(tier);
        clearMiningProgress(player);
        handleMainBlock(player);
    }

    public CustomItem getSelectedBlockDrop(Player player){
        return BlockType.values()[getSelectedBlockTier(player)].getBlock().blockDrop();
    }

    public double getBlockUnlockCost(Player player){
        return BlockType.values()[getBlockTier(player).ordinal() + 1].getBlock().unlockCost();
    }

    private void clearMiningProgress(Player player){
        Block block = selectedBlockMap.getSelectedBlock(player.getUniqueId());
        if(block == null || !miningMap.containsBlockLocation(block.getLocation()) || !miningMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))return;
        blockProgress.sendCleanPacket(player, block);
        miningMap.removeBlockPos(block.getLocation(), player.getUniqueId());
    }

    public void handleMainBlock(Player player){
        World world = player.getWorld();
        ProtectedRegion region = massBlockPacketSender.getRegion(world, MAIN_BLOCK_REGION);
        massBlockPacketSender.createBlockReplacementMap(player, world, region, Map.of(Material.SPONGE, BlockType.values()[getSelectedBlockTier(player)].getBlock().material()));
    }
}
