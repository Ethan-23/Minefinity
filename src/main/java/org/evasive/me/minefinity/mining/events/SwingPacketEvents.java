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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.data.MiningBlockData;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.data.SelectedBlockMap;
import org.evasive.me.minefinity.mining.handlers.BlockProgressHandler;
import org.evasive.me.minefinity.mining.utils.AnimationIDs;

import java.util.Objects;
import java.util.UUID;

public class SwingPacketEvents extends PacketListenerAbstract {

    private final BlockProgressHandler blockProgressHandler;
    private final CustomItemRegistryService customItemRegistryService;
    private final SelectedBlockMap selectedBlockMap;
    private final MiningDataMap miningDataMap;
    private final AnimationIDs animationIDs;

    private final static int PACKET_ID = 0x3C;

    public SwingPacketEvents(
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
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getPlayer();

        handleDiggingPacket(event);

        if(!isMiningAttemptValid(player, event)) return;

        Block block = selectedBlockMap.getSelectedBlock(player.getUniqueId());

        if(checkForIncorrectBlock(block)) return;

        Bukkit.getScheduler().runTask(Minefinity.getCore(), () -> handleMiningProgress(player, block));
    }

    public void handleDiggingPacket(PacketReceiveEvent event){

        if(event.getPacketType() != PacketType.Play.Client.PLAYER_DIGGING) return;

        Player player = event.getPlayer();

        if(!checkForCorrectGameMode(player)) return;

        WrapperPlayClientPlayerDigging digging = new WrapperPlayClientPlayerDigging(event);
        Vector3i blockPosition = digging.getBlockPosition();
        Block block = player.getWorld().getBlockAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());

        if(digging.getAction() != DiggingAction.START_DIGGING) return;

        if (checkForIncorrectBlock(block)) return;

        event.setCancelled(true);

        selectedBlockMap.addSelectedBlock(player.getUniqueId(), block);
    }

    public boolean isMiningAttemptValid(Player player, PacketReceiveEvent event){
        return validateTargetBlock(player, event) && validateItemInHand(player.getInventory().getItemInMainHand()) && checkForCorrectGameMode(player);
    }

    public boolean validateTargetBlock(Player player, PacketReceiveEvent event){
        if(event.getPacketId() != PACKET_ID || player.getGameMode() != GameMode.SURVIVAL || player.getTargetBlockExact(5) == null || selectedBlockMap.getSelectedBlock(player.getUniqueId()) == null)
            return false;

        return selectedBlockMap.getSelectedBlock(player.getUniqueId()).getLocation().equals(Objects.requireNonNull(player.getTargetBlockExact(5)).getLocation());
    }

    public boolean validateItemInHand(ItemStack itemStack){
        return customItemRegistryService.getRegisteredBaseItem(itemStack) instanceof BasePickaxeItem || itemStack.isEmpty();
    }

    public boolean checkForCorrectGameMode(Player player){
        return player.getGameMode().equals(GameMode.SURVIVAL);
    }

    public void handleMiningProgress(Player player, Block block){
        if(!miningDataMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))
            addBlockLocation(player.getUniqueId(), block.getLocation());

        if(checkForIncorrectBlock(block)) return;

        blockProgressHandler.addBlockProgress(block.getLocation(), player);
    }

    public void addBlockLocation(UUID uuid, Location location){
        miningDataMap.addMiningData(location, uuid, new MiningBlockData(animationIDs.getUniqueAnimationId(), 0));
    }

    public boolean checkForIncorrectBlock(Block block){
        return !block.getType().equals(Material.SPONGE);
    }
}
