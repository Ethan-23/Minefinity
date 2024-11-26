package org.evasive.me.minevolutionCore.worldPackets;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.evasive.me.minevolutionCore.MinevolutionCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BlockPacketEvents extends PacketListenerAbstract {

    final List<Material> fakeBlocks = new ArrayList<>(Arrays.asList(Material.WARPED_STEM, Material.WARPED_FENCE, Material.WARPED_SLAB, Material.WARPED_PLANKS));

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = (Player) event.getPlayer();
        if(player != null && player.getGameMode() == GameMode.CREATIVE)
            return;

        if (event.getPacketType() == PacketType.Play.Client.USE_ITEM) {
            PacketReceiveEvent copy = event.clone();
            WrapperPlayClientUseItem packet = new WrapperPlayClientUseItem(copy);

            Block block = player.getTargetBlockExact(5);
            if (block != null && block.getType() != Material.SPONGE && !fakeBlocks.contains(block.getType())) return;
            event.setCancelled(true);
        }

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            PacketReceiveEvent copy = event.clone();
            WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(copy);

            Block block = new Location(player.getWorld(), packet.getBlockPosition().getX(), packet.getBlockPosition().getY(), packet.getBlockPosition().getZ()).getBlock();

            if (block.getType() == Material.SPONGE || fakeBlocks.contains(block.getType())) {
                event.setCancelled(true);

                Bukkit.getScheduler().runTask(MinevolutionCore.getCore(), ()-> new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, player.getEquipment().getItemInMainHand(), block, player.getTargetBlockFace(5)).callEvent());}
            copy.cleanUp();

        }



    }

}
