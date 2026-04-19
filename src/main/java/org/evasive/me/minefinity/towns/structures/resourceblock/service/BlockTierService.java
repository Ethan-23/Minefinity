package org.evasive.me.minefinity.towns.structures.resourceblock.service;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.data.SelectedBlockMap;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;
import org.evasive.me.minefinity.towns.worldPackets.service.MassBlockPacketSender;

import java.util.List;
import java.util.Map;


public class BlockTierService {

    public final String MAIN_BLOCK_REGION = "minefinity_block";
    private final PlayerDataService playerDataService;
    private final MassBlockPacketSender massBlockPacketSender = new MassBlockPacketSender();
    private final MiningDataMap miningDataMap;
    private final SelectedBlockMap selectedBlockMap;
    private final CustomItemRegistryService customItemRegistryService;
    private final BlockTypeRegistryService blockTypeRegistryService;

    public BlockTierService(PlayerDataService playerDataService, CustomItemRegistryService customItemRegistryService, BlockTypeRegistryService blockTypeRegistryService, MiningDataMap miningDataMap, SelectedBlockMap selectedBlockMap) {
        this.playerDataService = playerDataService;
        this.miningDataMap = miningDataMap;
        this.selectedBlockMap = selectedBlockMap;
        this.customItemRegistryService = customItemRegistryService;
        this.blockTypeRegistryService = blockTypeRegistryService;
    }

    private PlayerData getPlayerData(Player player) {
        return playerDataService.getPlayerData(player.getUniqueId());
    }

    public String getSelectedMiningBlock(Player player, String worldId) {
        return getPlayerData(player).getSelectedBlockTier(worldId);
    }

    public BaseBlock getSelectedBaseBlock(Player player){
        String worldName = player.getWorld().getName();
        return blockTypeRegistryService.getBaseBlock(getPlayerData(player).getSelectedBlockTier(worldName));
    }

    public int getUnlockedMiningBlock(Player player, String worldId) {
        return getPlayerData(player).getUnlockedBlockTier(worldId);
    }

    public void setSelectedMiningBlock(Player player, String worldName, int blockTier){
        getPlayerData(player).setSelectedBlockTier(worldName, blockTypeRegistryService.getBlockIdByTier(worldName, blockTier));

        if(player.getWorld().getName().equals(worldName))
            handleMainBlock(player);

        clearMiningProgress(player);
    }

    public void setUnlockedMiningBlock(Player player, String worldName, int blockNumber){
        getPlayerData(player).setUnlockedBlockTier(worldName, blockNumber);
        setSelectedMiningBlock(player, worldName, blockNumber);
    }

    public ItemStack getSelectedBlockDrop(Player player){
        String selectedBlockId = getPlayerData(player).getSelectedBlockTier(player.getWorld().getName());
        BaseBlock baseBlock = blockTypeRegistryService.getBaseBlock(selectedBlockId);
        return customItemRegistryService.getRegisteredItemStack(baseBlock.blockDropId());
    }

    public ItemStack getSelectedSpecialDrop(Player player){
        String specialBlockId = getPlayerData(player).getSelectedBlockTier(player.getWorld().getName());
        BaseBlock baseBlock = blockTypeRegistryService.getBaseBlock(specialBlockId);
        return customItemRegistryService.getRegisteredItemStack(baseBlock.specialBlockDropId());
    }

    public double getBlockUnlockCost(Player player, String worldId){
        return blockTypeRegistryService.getNextUnlockCost(getPlayerData(player).getUnlockedBlockTier(worldId), worldId);
    }

    //Move to where swapping block tiers is
    public void clearMiningProgress(Player player){
        Block block = selectedBlockMap.getSelectedBlock(player.getUniqueId());
        if(block == null || !miningDataMap.containsBlockLocation(block.getLocation()) || !miningDataMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))return;
        sendCleanPacket(player, block);
        miningDataMap.removeBlockPos(block.getLocation(), player.getUniqueId());
    }

    public void handleMainBlock(Player player){
        World world = player.getWorld();
        ProtectedRegion region = massBlockPacketSender.getRegion(world, MAIN_BLOCK_REGION);
        massBlockPacketSender.createBlockReplacementMap(player, world, region, Map.of(Material.SPONGE, blockTypeRegistryService.getBaseBlock(getPlayerData(player).getSelectedBlockTier(player.getWorld().getName())).material()));
    }

    private void sendCleanPacket(Player player, Block block){
        WrapperPlayServerBlockBreakAnimation progressAnimation = new WrapperPlayServerBlockBreakAnimation(
                miningDataMap.getBlockAnimationID(block.getLocation(), player.getUniqueId()),
                new Vector3i(block.getX(), block.getY(), block.getZ()),
                (byte) -1
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, progressAnimation);
    }

    public List<String> getBlockTrackWorlds(){
        return blockTypeRegistryService.getWorldList();
    }

    public BlockTypeRegistryService getBlockTypeRegistryService() {
        return blockTypeRegistryService;
    }

    public boolean hasWorldUnlocked(Player player, String worldId){
        return getPlayerData(player).hasWorldUnlocked(worldId);
    }
}
