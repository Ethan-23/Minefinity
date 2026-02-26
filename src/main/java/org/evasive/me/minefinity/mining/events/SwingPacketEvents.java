package org.evasive.me.minefinity.mining.events;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.framework.ItemFunctions;
import org.evasive.me.minefinity.mining.service.BlockProgress;
import org.evasive.me.minefinity.mining.service.MiningBlockData;

import java.util.Objects;
import java.util.UUID;



public class SwingPacketEvents extends PacketListenerAbstract {

    public BlockProgress blockProgress = new BlockProgress();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getPlayer();

        handleDiggingPacket(event);

        if(!isMiningAttemptValid(player, event)) return;

        Block block = Minefinity.selectedBlockMap.getSelectedBlock(player.getUniqueId());

        if(checkForIncorrectBlock(block)) return;

        handleMiningProgress(player, block);
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

        Minefinity.selectedBlockMap.addSelectedBlock(player.getUniqueId(), block);
    }

    public boolean isMiningAttemptValid(Player player, PacketReceiveEvent event){
        return validateTargetBlock(player, event) && validateItemInHand(player.getInventory().getItemInMainHand()) && checkForCorrectGameMode(player);
    }

    public boolean validateTargetBlock(Player player, PacketReceiveEvent event){
        if(event.getPacketId() != 0x3C || player.getGameMode() != GameMode.SURVIVAL || player.getTargetBlockExact(5) == null || Minefinity.selectedBlockMap.getSelectedBlock(player.getUniqueId()) == null)
            return false;

        return Minefinity.selectedBlockMap.getSelectedBlock(player.getUniqueId()).getLocation().equals(Objects.requireNonNull(player.getTargetBlockExact(5)).getLocation());
    }

    public boolean validateItemInHand(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemStack.hasItemMeta() && itemMeta != null && new ItemFunctions().hasItemId(itemStack) && new ItemFunctions().isPickaxe(itemStack);
    }

    public boolean checkForCorrectGameMode(Player player){
        return player.getGameMode().equals(GameMode.SURVIVAL);
    }

    public void handleMiningProgress(Player player, Block block){
        if(!Minefinity.miningMap.containsPlayerAtLocation(block.getLocation(), player.getUniqueId()))
            addBlockLocation(player.getUniqueId(), block.getLocation());

        if(checkForIncorrectBlock(block)) return;

        blockProgress.addBlockProgress(block.getLocation(), player);
    }

    public void addBlockLocation(UUID uuid, Location location){
        Minefinity.miningMap.addMiningData(location, uuid, new MiningBlockData(Minefinity.animationIDs.getUniqueAnimationId(), 0));
    }

    public boolean checkForIncorrectBlock(Block block){
        return !block.getType().equals(Material.SPONGE);
    }
}
