package org.evasive.me.minevolutionCore.npcs;

import com.github.retrooper.packetevents.protocol.world.Location;

import java.util.UUID;

public class NPC {

    private UUID uuid;
    private int entityID;
    private String name;
    private Location location;

    public static NPC createNPC(UUID uuid, int entityID, String name, Location location){
        NPC npc = new NPC();
        npc.uuid = uuid;
        npc.entityID = entityID;
        npc.name = name;
        npc.location = location;
        return npc;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }
}
