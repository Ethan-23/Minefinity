package org.evasive.me.minefinity.core.npcs;

import com.github.retrooper.packetevents.protocol.world.Location;
import org.bukkit.entity.ArmorStand;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NpcInstance {

    UUID npcUuid;
    int entityId;
    NpcType type;           // behavior / skin
    Set<UUID> viewers;      // which players currently see it
    Location loc;
    ArmorStand stand;

    public NpcInstance(UUID npcUuid, int entityId, NpcType type, Location loc, ArmorStand stand) {
        this.npcUuid = npcUuid;
        this.entityId = entityId;
        this.type = type;
        this.viewers = new HashSet<>();
        this.loc = loc;
        this.stand = stand;
    }

    public UUID getNpcUuid() {
        return npcUuid;
    }

    public void setNpcUuid(UUID entityUuid) {
        this.npcUuid = entityUuid;
    }

    public NpcType getType() {
        return type;
    }

    public void setType(NpcType type) {
        this.type = type;
    }

    public Set<UUID> getViewers() {
        return viewers;
    }

    public void setViewers(Set<UUID> viewers) {
        this.viewers = viewers;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public ArmorStand getStand() {
        return stand;
    }

    public void setStand(ArmorStand stand) {
        this.stand = stand;
    }
}
