package org.evasive.me.minefinity.npcs;

import java.util.*;

public class NpcInstanceMap {

    private final Map<Integer, UUID> entityIdToUuid = new HashMap<>();
    private final Map<UUID, NpcInstance> npcInstances = new HashMap<>();

    public void addNpc(NpcInstance npc) {
        entityIdToUuid.put(npc.entityId, npc.npcUuid);
        npcInstances.put(npc.npcUuid, npc);
    }

    public void removeNpc(UUID entityUuid) {
        entityIdToUuid.remove(npcInstances.get(entityUuid).getEntityId());
        npcInstances.remove(entityUuid);
    }

    public boolean hasNpc(UUID entityUuid) {
        return npcInstances.containsKey(entityUuid);
    }

    public NpcInstance getNpc(int entityId) {
        return npcInstances.get(entityIdToUuid.get(entityId));
    }

    public NpcInstance getNpc(UUID uuid) {
        return npcInstances.get(uuid);
    }

    public void addViewer(UUID entityUuid, UUID uuid){
        npcInstances.get(entityUuid).viewers.add(uuid);
    }

    public boolean containsViewer(UUID entityUuid, UUID uuid){
        return npcInstances.get(entityUuid).viewers.contains(uuid);
    }

    public void removeViewer(UUID entityUuid, UUID uuid){
        npcInstances.get(entityUuid).viewers.remove(uuid);
    }

    public UUID getEntityUuid(int entityId){
        return entityIdToUuid.get(entityId);
    }
}
