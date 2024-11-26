package org.evasive.me.minevolutionCore.npcs;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.world.Location;

import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NPCManager {

    final NPCTracker npcTracker = new NPCTracker();

    public void spawnCustomNPC(Player player, Location location, String customName, TextureProperty textureProperty) {

        UUID uuid = UUID.randomUUID();
        int entityId = SpigotReflectionUtil.generateEntityId();

        List<TextureProperty> textureProperties = new ArrayList<>();
        textureProperties.add(textureProperty);
        UserProfile userProfile = new UserProfile(uuid, customName, textureProperties);

        WrapperPlayServerPlayerInfoUpdate.PlayerInfo playerDataUpdate = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                userProfile,
                true,
                0,
                GameMode.CREATIVE,
                Component.text(customName),
                null
        );

        WrapperPlayServerPlayerInfoUpdate addPlayerPacket = new WrapperPlayServerPlayerInfoUpdate(
                WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
                playerDataUpdate
        );

        WrapperPlayServerPlayerInfoUpdate addPlayerListed = new WrapperPlayServerPlayerInfoUpdate(
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED,
                playerDataUpdate
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, addPlayerPacket);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, addPlayerListed);

        WrapperPlayServerSpawnEntity spawnPlayer = new WrapperPlayServerSpawnEntity(
                entityId,
                Optional.of(uuid),
                EntityTypes.PLAYER,
                location.getPosition(),
                location.getPitch(),
                location.getYaw(),
                location.getYaw(),
                0,
                Optional.empty()
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPlayer);

        List<EntityData> metadataList = new ArrayList<>();
        metadataList.add(new EntityData(17, EntityDataTypes.BYTE, (byte) 0xff));

        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(entityId, metadataList);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, metadataPacket);

        NPC npc = NPC.createNPC(uuid, entityId, customName, location);
        getNpcTracker().addNpc(player.getUniqueId(), entityId, npc);
    }

    public void lookAtPlayer(Player player, NPC npc){
        org.bukkit.Location location = player.getLocation();

        double deltaX = location.getX() - npc.getLocation().getX();
        double deltaY = location.getY() - npc.getLocation().getY();
        double deltaZ = location.getZ() - npc.getLocation().getZ();

        double yawRadians = Math.atan2(deltaZ, deltaX);

        float newYaw = (float) Math.toDegrees(yawRadians);

        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ); // Horizontal distance (X and Z)
        double pitchRadians = Math.atan2(deltaY, distanceXZ);
        float newPitch = (float) -Math.toDegrees(pitchRadians);

        newYaw = (newYaw + 360) % 360;

        WrapperPlayServerEntityRelativeMoveAndRotation npcLook = new WrapperPlayServerEntityRelativeMoveAndRotation(
                npc.getEntityID(),
                0,
                0,
                0,
                newYaw - 90,
                newPitch,
                true
        );

        WrapperPlayServerEntityHeadLook npcHeadLook = new WrapperPlayServerEntityHeadLook(
                npc.getEntityID(),
                newYaw - 90
        );


        PacketEvents.getAPI().getPlayerManager().sendPacket(player, npcLook);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, npcHeadLook);

    }

    public NPCTracker getNpcTracker(){
        return npcTracker;
    }

}
