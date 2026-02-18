package org.evasive.me.minefinity.worldPackets;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.evasive.me.minefinity.Minefinity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BlockPacketEvents extends PacketListenerAbstract {

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

            if (!isBlockInAnyRegion(block)) return;

            event.setCancelled(true);
        }

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            PacketReceiveEvent copy = event.clone();
            WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(copy);

            Block block = new Location(player.getWorld(), packet.getBlockPosition().getX(), packet.getBlockPosition().getY(), packet.getBlockPosition().getZ()).getBlock();

            if (isBlockInAnyRegion(block)) {
                event.setCancelled(true);

                Bukkit.getScheduler().runTask(Minefinity.getCore(), ()-> new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, player.getEquipment().getItemInMainHand(), block, player.getTargetBlockFace(5)).callEvent());
            }
            copy.cleanUp();

        }
    }

    public boolean isBlockInAnyRegion(Block block) {
        World world = block.getWorld();

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        if (regions == null) return false;

        BlockVector3 vec = BlockVector3.at(block.getX(), block.getY(), block.getZ());
        ApplicableRegionSet set = regions.getApplicableRegions(vec);

        return set.size() > 0;
    }
}
