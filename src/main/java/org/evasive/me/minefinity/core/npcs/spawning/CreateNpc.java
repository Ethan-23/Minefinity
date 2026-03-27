package org.evasive.me.minefinity.core.npcs.spawning;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.npcs.NpcInstance;
import org.evasive.me.minefinity.core.npcs.NpcInstanceMap;
import org.evasive.me.minefinity.core.npcs.NpcType;

import java.util.UUID;

import static org.evasive.me.minefinity.core.utils.EntityIdUtil.getNextEntityId;
import static org.evasive.me.minefinity.core.utils.LocationConvert.convertLocation;

public class CreateNpc {

    public final static NamespacedKey NPC_TYPE_KEY = new NamespacedKey(Minefinity.getCore(), "npc_type");
    public final static NamespacedKey NPC_UUID_KEY = new NamespacedKey(Minefinity.getCore(), "npc_uuid");
    private final NpcInstanceMap npcInstanceMap;

    public CreateNpc(NpcInstanceMap npcInstanceMap) {
        this.npcInstanceMap = npcInstanceMap;
    }

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
