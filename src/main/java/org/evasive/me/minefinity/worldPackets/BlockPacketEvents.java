package org.evasive.me.minefinity.worldPackets;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.evasive.me.minefinity.Minefinity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BlockPacketEvents extends PacketListenerAbstract {

    final List<Material> fakeBlocks = new ArrayList<>(Arrays.asList(Material.WARPED_STEM, Material.WARPED_FENCE, Material.WARPED_SLAB, Material.WARPED_PLANKS));

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getPlayer();

        //Player is null sometimes
        if(player == null) return;

        if(player.getGameMode() == GameMode.CREATIVE)
            return;

        if (event.getPacketType() == PacketType.Play.Client.USE_ITEM) {
            Block block = player.getTargetBlockExact(5);

            if(block == null) return;

            if (block.getType() != Material.SPONGE || !fakeBlocks.contains(block.getType())) return;

            event.setCancelled(true);
        }

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            PacketReceiveEvent copy = event.clone();
            WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(copy);

            Block block = new Location(player.getWorld(), packet.getBlockPosition().getX(), packet.getBlockPosition().getY(), packet.getBlockPosition().getZ()).getBlock();

            if (block.getType() == Material.SPONGE || fakeBlocks.contains(block.getType())) {
                event.setCancelled(true);
                //Random error fix later
                Bukkit.getScheduler().runTask(Minefinity.getCore(), ()-> new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, player.getEquipment().getItemInMainHand(), block, player.getTargetBlockFace(5)).callEvent());
            }
            copy.cleanUp();

        }



    }

}
