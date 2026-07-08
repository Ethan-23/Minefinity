package org.evasive.me.minefinity.mining.abilities.criticalfracture;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.abilities.criticalfracture.data.CriticalMap;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;

import java.util.UUID;

public class CriticalFracture implements MiningAbility {

    private final CriticalMap criticalMap;

    private final static float SPEED_MULTIPLIER = 1.25f;
    private final static long MAX_CRIT_TIME = 5000;//ms

    private static final float BLOCK_CENTER_OFFSET = 0.5f;
    private static final float BLOCK_OUTER_OFFSET = 0.8f;
    private static final float BLOCK_FACE_OFFSET = 0.05f;

    public CriticalFracture(CriticalMap criticalMap) {
        this.criticalMap = criticalMap;
    }

    @Override
    public void applyStats(HitContext context) {
        UUID uuid = context.getUUID();
        if(criticalMap.containsCritical(uuid) && criticalMap.getCritical(uuid).isHit())
            context.getStatsContext().multiplySpeed(SPEED_MULTIPLIER);
    }

    @Override
    public void onHit(HitContext context) {

        Player player = context.getPlayer();
        UUID uuid = context.getUUID();

        if(criticalMap.containsCritical(uuid) && criticalMap.getCritical(uuid).isHit()){
            criticalMap.getCritical(uuid).setCreationTime();
            return;
        }

        RayTraceResult rayTraceResult = player.rayTraceBlocks(5);
        if(rayTraceResult == null) return;
        Location hitLoc = rayTraceResult.getHitPosition().toLocation(player.getWorld());

        if(!criticalMap.containsCritical(uuid)){

            BlockFace face = rayTraceResult.getHitBlockFace();
            Block block = rayTraceResult.getHitBlock();
            if(block == null || face == null) return;

            Location critLocation = generateRandomCritLocation(block, face);

            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Minefinity.getCore(), () -> {
                sendCriticalPacket(player, critLocation);
                if(System.currentTimeMillis() - criticalMap.getCritical(uuid).getCreationTime() > MAX_CRIT_TIME)
                    criticalMap.removeCritical(uuid);
            }, 0L, 4L);
            criticalMap.addCritical(player.getUniqueId(), critLocation, task);
            return;
        }

        criticalMap.getCritical(uuid).setCreationTime();

        if(criticalMap.getCritical(uuid).isInside(hitLoc)){
            player.playSound(player.getLocation(), Sound.UI_HUD_BUBBLE_POP, 1f, 1f);
            sendHitCriticalPacket(player);
            criticalMap.getCritical(uuid).getRepeatingTask().cancel();
            criticalMap.hitCritical(uuid, null);
        }

    }

    private Location generateRandomCritLocation(Block block, BlockFace face){

        Location loc = block.getLocation().add(BLOCK_CENTER_OFFSET, BLOCK_CENTER_OFFSET, BLOCK_CENTER_OFFSET);

        double randX = (Math.random() - BLOCK_CENTER_OFFSET) * BLOCK_OUTER_OFFSET;
        double randY = (Math.random() - BLOCK_CENTER_OFFSET) * BLOCK_OUTER_OFFSET;
        double randZ = (Math.random() - BLOCK_CENTER_OFFSET) * BLOCK_OUTER_OFFSET;

        switch (face) {

            case NORTH -> {
                loc.setZ(block.getZ());
                loc.add(randX, randY, 0);
            }

            case SOUTH -> {
                loc.setZ(block.getZ() + 1);
                loc.add(randX, randY, 0);
            }

            case EAST -> {
                loc.setX(block.getX() + 1);
                loc.add(0, randY, randZ);
            }

            case WEST -> {
                loc.setX(block.getX());
                loc.add(0, randY, randZ);
            }

            case UP -> {
                loc.setY(block.getY() + 1);
                loc.add(randX, 0, randZ);
            }

            case DOWN -> {
                loc.setY(block.getY());
                loc.add(randX, 0, randZ);
            }
        }
        loc.add(
                face.getModX() * BLOCK_FACE_OFFSET,
                face.getModY() * BLOCK_FACE_OFFSET,
                face.getModZ() * BLOCK_FACE_OFFSET
        );
        return loc;
    }

    private void sendCriticalPacket(Player player, Location location){

        WrapperPlayServerParticle packet = new WrapperPlayServerParticle(
                new Particle<>(ParticleTypes.CRIT),
                true,
                new Vector3d(location.x(), location.y(), location.z()),
                new Vector3f(0, 0, 0),
                0f,
                1,
                true
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);

    }

    private void sendHitCriticalPacket(Player player){
        UUID uuid = player.getUniqueId();
        Location location = criticalMap.getCritical(uuid).getCenter();

        WrapperPlayServerParticle packet = new WrapperPlayServerParticle(
                new Particle<>(ParticleTypes.BUBBLE_POP),
                true,
                new Vector3d(location.x(), location.y(), location.z()),
                new Vector3f(0, 0, 0),
                0f,
                1,
                true
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    private void removeCriticalPacket(UUID uuid){
        this.criticalMap.removeCritical(uuid);
    }

    @Override
    public void onBreak(BreakContext context) {
        removeCriticalPacket(context.getUUID());
    }
}
