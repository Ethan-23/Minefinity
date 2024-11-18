package org.evasive.me.minevolutionCore.npcs;

import com.github.retrooper.packetevents.protocol.world.Location;
import org.bukkit.Bukkit;

import java.util.*;

public class NPCTracker {

    //Player UUID, NPC EntityID #, NPC Information
    private HashMap<UUID, HashMap<Integer, NPC>> npcs = new HashMap<>();

    public List<NPC> getPlayerNpcList(UUID uuid){
        return npcs.getOrDefault(uuid, null).values().stream().toList();
    }

    public void addPlayer(UUID uuid){
        npcs.computeIfAbsent(uuid, map -> new HashMap<>());
    }

    public void addNpc(UUID uuid, int entityID, NPC npc){
        if(!npcs.containsKey(uuid))
            return;
        npcs.get(uuid).put(entityID, npc);
    }

    public NPC getNPC(UUID uuid, int entityID){
        if(!npcs.containsKey(uuid))
            return null;
        return npcs.get(uuid).get(entityID);
    }
}
