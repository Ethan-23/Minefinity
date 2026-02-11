package org.evasive.me.minevolutionCore.npcs.spawning;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.npcs.NpcInstance;
import org.evasive.me.minevolutionCore.npcs.npc.NpcType;

import java.util.UUID;

import static org.evasive.me.minevolutionCore.MinevolutionCore.npcInstanceMap;
import static org.evasive.me.minevolutionCore.utils.EntityIdUtil.getNextEntityId;
import static org.evasive.me.minevolutionCore.utils.LocationConvert.convertLocation;

public class CreateNpc {

    public final static NamespacedKey NPC_TYPE_KEY = new NamespacedKey(MinevolutionCore.getCore(), "npc_type");
    public final static NamespacedKey NPC_UUID_KEY = new NamespacedKey(MinevolutionCore.getCore(), "npc_uuid");

    private void createArmorStand(World world, Location loc, NpcType npcType){

        ArmorStand npcMarker = world.spawn(loc, ArmorStand.class, as -> {
           as.setInvulnerable(true);
           as.setGravity(false);
           as.setInvisible(true);
           as.setBasePlate(false);
           as.setMarker(true);
           as.setCustomNameVisible(false);
           as.addScoreboardTag("npc_marker");
           as.getPersistentDataContainer().set(NPC_TYPE_KEY, PersistentDataType.STRING, npcType.name());
           as.getPersistentDataContainer().set(NPC_UUID_KEY, PersistentDataType.STRING, UUID.randomUUID().toString());
        });

        setupNpcFromArmorStand(npcMarker);
    }

    public void createNewNPC(World world, Location loc, NpcType npcType){
        createArmorStand(world, loc, npcType);
    }

    public void setupNpcFromArmorStand(ArmorStand stand) {

        int entityId = getNextEntityId();

        String npcTypeString = stand.getPersistentDataContainer().get(NPC_TYPE_KEY, PersistentDataType.STRING);

        if(npcTypeString == null || npcTypeString.isEmpty()) return;

        String uuid = stand.getPersistentDataContainer().get(NPC_UUID_KEY, PersistentDataType.STRING);
        NpcType npcType = NpcType.valueOf(npcTypeString.toUpperCase());
        NpcInstance npc = new NpcInstance(UUID.fromString(uuid), entityId, npcType, convertLocation(stand.getLocation()), stand);
        npcInstanceMap.addNpc(npc);

    }

}
