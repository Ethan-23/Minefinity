package org.evasive.me.minevolutionCore.mining;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.blocks.BlockDataFunctions;
import org.evasive.me.minevolutionCore.customItems.pickaxes.PickaxeStatFunctions;
import org.evasive.me.minevolutionCore.utils.PickaxeKeys;

public class MiningAnimation extends PacketListenerAbstract {

    final MiningMap miningMap = new MiningMap();
    final MiningFunctions miningFunctions = new MiningFunctions();
    final PickaxeStatFunctions pickaxeStatFunctions = new PickaxeStatFunctions();
    final BlockDataFunctions blockDataFunctions = new BlockDataFunctions();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        Player player = event.getPlayer();

        //0x36 Arm Swing Packet
        if(event.getPacketId() == 0x36 && miningMap.hasPlayer(player)){
            startSwing(event.getPlayer());
        }

        if(event.getPacketType() != PacketType.Play.Client.PLAYER_DIGGING)
            return;

        WrapperPlayClientPlayerDigging digging = new WrapperPlayClientPlayerDigging(event);
        Vector3i blockPosition = digging.getBlockPosition();
        Block block = player.getWorld().getBlockAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());

        if(block.getType() != Material.SPONGE)
            return;

        switch (digging.getAction()){
            case START_DIGGING:
                event.setCancelled(true);
                startBreakAnimation(player, block);
                break;
            case CANCELLED_DIGGING:
                if(miningMap.hasPlayer(player))
                    miningMap.setMining(player, false);
                event.setCancelled(true);
                cancelBreakAnimation(player, block);
                break;
            case FINISHED_DIGGING:
                finishBreakAnimation(player, block);
                break;
        }
    }

    public void startBreakAnimation(Player player, Block block) {

        ItemStack item = player.getInventory().getItemInMainHand();

        if(!miningMap.hasPlayer(player)) {
            miningMap.addPlayer(player, new MiningPlayer(true, true, 0, null, null));
        }

        //miningMap.setMining(player, true);

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR || pickaxeStatFunctions.notHoldingPickaxe(item.getItemMeta()))
            return;

        ItemMeta meta = item.getItemMeta();

        miningMap.setMining(player, true);

        miningMap.addProgress(player, calculateMiningSpeed(meta, player));

        byte blockProgress = (byte) (miningMap.getProgress(player)/(blockDataFunctions.getBlockHealth(player) / 10));

        //Send Break Animation Packet

        miningMap.setMiningTask(player, createBreakingTask(player, block, blockProgress));
    }

    public BukkitTask createBreakingTask(Player player, Block block, byte blockProgress){
        return Bukkit.getScheduler().runTaskLater(MinevolutionCore.getCore(), () -> {

            sendAnimationPacket(player, block, blockProgress);

            Vector3i blockLocation = new Vector3i(block.getX(), block.getY(), block.getZ());

            if(!miningMap.hasPlayer(player)){
                sendCancelPacket(player, blockLocation);
                return;
            }

            if(miningMap.getProgress(player) <= blockDataFunctions.getBlockHealth(player)){
                sendStartPacket(player, blockLocation);
            }else{
                sendFinishPacket(player, blockLocation);
            }
        }, 1L);
    }

    public void cancelBreakAnimation(Player player, Block block) {
        if(!miningMap.hasPlayer(player)){
            return;
        }

        if(miningMap.getMiningTask(player) != null)
            miningMap.getMiningTask(player).cancel();

        removeSwingTimer(player);
        miningMap.removePlayer(player);

        sendAnimationPacket(player, block, (byte) -1);
    }

    public void finishBreakAnimation(Player player, Block block) {
        if(!miningMap.hasPlayer(player)){
            return;
        }

        miningFunctions.mainBlockMined(player, block);
        miningMap.setProgress(player, 0);
        Vector3i blockLocation = new Vector3i(block.getX(), block.getY(), block.getZ());
        if(miningMap.isMining(player)){
            sendStartPacket(player, blockLocation);
        } else {
            sendCancelPacket(player, blockLocation);
        }
    }

    //Packets

    public void sendStartPacket(Player player, Vector3i location){
        if(!miningMap.isSwinging(player))
            return;
        WrapperPlayClientPlayerDigging clientPlayerStartDigging = new WrapperPlayClientPlayerDigging(
                DiggingAction.START_DIGGING,
                location,
                BlockFace.EAST,
                0
        );
        PacketEvents.getAPI().getPlayerManager().receivePacket(player, clientPlayerStartDigging);
    }

    public void sendCancelPacket(Player player, Vector3i location){
        WrapperPlayClientPlayerDigging clientPlayerStartDigging = new WrapperPlayClientPlayerDigging(
                DiggingAction.CANCELLED_DIGGING,
                location,
                BlockFace.EAST,
                0
        );
        PacketEvents.getAPI().getPlayerManager().receivePacket(player, clientPlayerStartDigging);
    }

    public void sendFinishPacket(Player player, Vector3i location){
        WrapperPlayClientPlayerDigging clientPlayerFinishedDigging = new WrapperPlayClientPlayerDigging(
                DiggingAction.FINISHED_DIGGING,
                location,
                BlockFace.EAST,
                0
        );
        PacketEvents.getAPI().getPlayerManager().receivePacket(player, clientPlayerFinishedDigging);
    }

    public void sendAnimationPacket(Player player, Block block, byte blockProgress){
        WrapperPlayServerBlockBreakAnimation progressAnimation = new WrapperPlayServerBlockBreakAnimation(
                Integer.MAX_VALUE,
                new Vector3i(block.getX(), block.getY(), block.getZ()),
                blockProgress
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, progressAnimation);
    }

    //Swing Functions

    public void startSwing(Player player) {
        miningMap.setSwinging(player, true);
        resetSwingTimer(player);
    }

    public void stopSwing(Player player) {
        miningMap.setSwinging(player, false);
    }

    private void resetSwingTimer(Player player) {
        // Cancel existing timer if any
        removeSwingTimer(player);

        // Start a new timer to cancel swing if no further packet arrives
        BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                stopSwing(player);
            }
        };
        timer.runTaskLater(MinevolutionCore.getCore(), 10L); // Adjust delay as needed
        miningMap.setSwingCooldown(player, timer);
    }

    private void removeSwingTimer(Player player){
        if (miningMap.hasPlayer(player) && miningMap.getSwingCooldown(player) != null) {
            miningMap.getSwingCooldown(player).cancel();
        }
    }

    public float calculateMiningSpeed(ItemMeta meta, Player player){
        float effectiveSpeed = pickaxeStatFunctions.getMiningSpeed(meta);
        effectiveSpeed = Math.min(effectiveSpeed, (float) new BlockDataFunctions().getBlockHealth(player) / 5f);

        if(miningFunctions.isSuperbreakerActive(player))
            effectiveSpeed += meta.getPersistentDataContainer().get(PickaxeKeys.baseSpeedKey, PersistentDataType.INTEGER);

        return effectiveSpeed;
    }

}
