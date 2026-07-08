package org.evasive.me.minefinity.mining.events;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.data.MiningBlockData;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.data.SelectedBlockMap;
import org.evasive.me.minefinity.mining.handlers.BlockProgressHandler;
import org.evasive.me.minefinity.mining.utils.AnimationIDs;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class SwingPacketListener extends PacketListenerAbstract {

    private final BlockProgressHandler blockProgressHandler;
    private final CustomItemRegistryService customItemRegistryService;
    private final SelectedBlockMap selectedBlockMap;
    private final MiningDataMap miningDataMap;
    private final AnimationIDs animationIDs;

    private final static int SWING_ARM_PACKET_ID = 0x3C;

    public SwingPacketListener(
            CustomItemRegistryService customItemRegistryService,
            SelectedBlockMap selectedBlockMap,
            MiningDataMap miningDataMap,
            AnimationIDs animationIDs,
            BlockProgressHandler blockProgressHandler)
    {
        this.blockProgressHandler = blockProgressHandler;
        this.customItemRegistryService = customItemRegistryService;
        this.selectedBlockMap = selectedBlockMap;
        this.miningDataMap = miningDataMap;
        this.animationIDs = animationIDs;
    }


    @Override
    public void onPacketReceive(@NonNull PacketReceiveEvent event) {

        boolean playerDigging = event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING;
        boolean swingArm = event.getPacketId() == SWING_ARM_PACKET_ID;

        if(!playerDigging && !swingArm)
            return;

        Player player = event.getPlayer();

        if(player.getGameMode() != GameMode.SURVIVAL)
            return;

        if(playerDigging)
            handleDiggingPacket(event);

        if(!swingArm)
            return;

        Bukkit.getScheduler().runTask(Minefinity.getCore(), () ->
                handleSwingOnServer(player));
    }

    private void handleSwingOnServer(Player player) {

        if(!player.isOnline())
            return;

        Block selectedBlock = selectedBlockMap.getSelectedBlock(player.getUniqueId());

        if(!isMiningToolValid(player) || !isBlockValid(player, selectedBlock))
            return;

        handleMiningProgress(player, selectedBlock);
    }

    public void handleDiggingPacket(PacketReceiveEvent event){

        WrapperPlayClientPlayerDigging digging = new WrapperPlayClientPlayerDigging(event);

        if(digging.getAction() != DiggingAction.START_DIGGING)
            return;

        Player player = event.getPlayer();
        Vector3i blockPos = digging.getBlockPosition();
        World world = player.getWorld();

        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        if(!world.isChunkLoaded(x >> 4, z >> 4))
            return;

        Block block = world.getBlockAt(x, y, z);

        if(block.getType() != Material.SPONGE)
            return;

        event.setCancelled(true);
        handleStartDiggingPacket(player, block);
    }

    private void handleStartDiggingPacket(Player player, Block block){
        selectedBlockMap.addSelectedBlock(player.getUniqueId(), block);
    }

    public boolean isMiningToolValid(Player player){
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        return customItemRegistryService.isPickaxe(itemInHand) || itemInHand.isEmpty();
    }

    private boolean isBlockValid(Player player, Block block) {
        Block targetBlock = player.getTargetBlockExact(5);
        if(targetBlock == null || block == null || !block.getType().equals(Material.SPONGE))
            return false;

        return block.getLocation().equals(targetBlock.getLocation());
    }

    public void handleMiningProgress(Player player, Block block){
        if(!miningDataMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))
            addBlockLocation(player.getUniqueId(), block.getLocation());

        blockProgressHandler.addBlockProgress(block.getLocation(), player);
    }

    public void addBlockLocation(UUID uuid, Location location){
        miningDataMap.addMiningData(location, uuid, new MiningBlockData(animationIDs.getUniqueAnimationId(), 0));
    }

}
