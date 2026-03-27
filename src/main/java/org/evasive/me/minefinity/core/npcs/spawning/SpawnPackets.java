package org.evasive.me.minefinity.core.npcs.spawning;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.npcs.NpcInstance;
import org.evasive.me.minefinity.core.npcs.NpcType;
import org.evasive.me.minefinity.core.utils.TextConversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.evasive.me.minefinity.core.utils.TextConversions.formatItemName;

public class SpawnPackets {

    public void sendNpcPackets(Player player, NpcInstance npcInstance, Location location) {

        String name = npcInstance.getType().getName();
        NpcType npcType = npcInstance.getType();

        List<TextureProperty> textureProperties = new ArrayList<>();
        textureProperties.add(new TextureProperty("textures", npcType.getSkin(), npcType.getSignature()));
        UserProfile userProfile = new UserProfile(npcInstance.getNpcUuid(), name, textureProperties);

        WrapperPlayServerPlayerInfoUpdate.PlayerInfo playerDataUpdate = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                userProfile,
                true,
                0,
                GameMode.CREATIVE,
                TextConversions.parse(formatItemName(name)),
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
        //PacketEvents.getAPI().getPlayerManager().sendPacket(player, addPlayerListed);

        WrapperPlayServerSpawnEntity spawnPlayer = new WrapperPlayServerSpawnEntity(
                npcInstance.getEntityId(),
                Optional.of(npcInstance.getNpcUuid()),
                EntityTypes.PLAYER,
                location.getPosition(),
                location.getPitch(),
                location.getYaw(),
                location.getYaw(),
                0,
                Optional.empty()
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPlayer);
    }

    public void despawnPacket(Player player, NpcInstance npcInstance) {

        UserProfile profile = new UserProfile(
                npcInstance.getNpcUuid(),
                npcInstance.getType().name()
        );

        WrapperPlayServerPlayerInfoUpdate.PlayerInfo info =
                new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                        profile,
                        false,
                        0,
                        GameMode.CREATIVE,
                        Component.empty(),
                        null
                );

        WrapperPlayServerPlayerInfoUpdate unlistPacket =
                new WrapperPlayServerPlayerInfoUpdate(
                        WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED,
                        info
                );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, unlistPacket);

        WrapperPlayServerDestroyEntities destroyPacket =
                new WrapperPlayServerDestroyEntities(
                        new int[]{npcInstance.getEntityId()}
                );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, destroyPacket);
    }

    public void lookAtPlayer(Player player, NpcInstance npc){
        org.bukkit.Location location = player.getLocation();

        double deltaX = location.getX() - npc.getLoc().getX();
        double deltaY = location.getY() - npc.getLoc().getY();
        double deltaZ = location.getZ() - npc.getLoc().getZ();

        double yawRadians = Math.atan2(deltaZ, deltaX);

        float newYaw = (float) Math.toDegrees(yawRadians);

        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ); // Horizontal distance (X and Z)
        double pitchRadians = Math.atan2(deltaY, distanceXZ);
        float newPitch = (float) -Math.toDegrees(pitchRadians);

        newYaw = (newYaw + 360) % 360;

        WrapperPlayServerEntityRelativeMoveAndRotation npcLook = new WrapperPlayServerEntityRelativeMoveAndRotation(
                npc.getEntityId(),
                0,
                0,
                0,
                newYaw - 90,
                newPitch,
                true
        );

        WrapperPlayServerEntityHeadLook npcHeadLook = new WrapperPlayServerEntityHeadLook(
                npc.getEntityId(),
                newYaw - 90
        );


        PacketEvents.getAPI().getPlayerManager().sendPacket(player, npcLook);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, npcHeadLook);

    }

}
