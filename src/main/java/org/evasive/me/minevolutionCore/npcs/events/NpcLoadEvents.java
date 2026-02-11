package org.evasive.me.minevolutionCore.npcs.events;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import io.papermc.paper.event.player.PlayerTrackEntityEvent;
import io.papermc.paper.event.player.PlayerUntrackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.npcs.NpcInstance;
import org.evasive.me.minevolutionCore.npcs.npc.NpcType;
import org.evasive.me.minevolutionCore.npcs.spawning.CreateNpc;
import org.evasive.me.minevolutionCore.npcs.spawning.SpawnPackets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.evasive.me.minevolutionCore.MinevolutionCore.npcInstanceMap;
import static org.evasive.me.minevolutionCore.npcs.spawning.CreateNpc.NPC_TYPE_KEY;
import static org.evasive.me.minevolutionCore.npcs.spawning.CreateNpc.NPC_UUID_KEY;
import static org.evasive.me.minevolutionCore.utils.LocationConvert.convertLocation;

public class NpcLoadEvents implements Listener {

    @EventHandler
    public void onPlayerTrackEntity(PlayerTrackEntityEvent e){

        if (!(e.getEntity() instanceof ArmorStand as) || !e.getEntity().getPersistentDataContainer().has(NPC_TYPE_KEY)) return;

        Player player = e.getPlayer();
        UUID npcUuid = UUID.fromString(Objects.requireNonNull(as.getPersistentDataContainer().get(NPC_UUID_KEY, PersistentDataType.STRING)));

        if(!npcInstanceMap.hasNpc(npcUuid)) new CreateNpc().setupNpcFromArmorStand(as);

        addPlayerView(npcUuid, player, as.getLocation());

    }

    public void addPlayerView(UUID npcUuid, Player player, Location location){

        new BukkitRunnable() {
            @Override
            public void run() {
                if(npcInstanceMap.containsViewer(npcUuid, player.getUniqueId())) return;

                NpcInstance npcInstance = npcInstanceMap.getNpc(npcUuid);

                new SpawnPackets().sendNpcPackets(player, npcInstance, convertLocation(location));

                npcInstanceMap.addViewer(npcUuid, player.getUniqueId());

            }
        }.runTaskLater(MinevolutionCore.getCore(), 10L);

    }

    @EventHandler
    public void onUntrack(PlayerUntrackEntityEvent e) {

        if (!(e.getEntity() instanceof ArmorStand as) || !e.getEntity().getPersistentDataContainer().has(NPC_TYPE_KEY)) return;

        Player player = e.getPlayer();
        UUID npcUuid = UUID.fromString(Objects.requireNonNull(as.getPersistentDataContainer().get(NPC_UUID_KEY, PersistentDataType.STRING)));

        removePlayerView(npcUuid, player, npcInstanceMap.getNpc(npcUuid));
    }

    public void removePlayerView(UUID npcUuid, Player player, NpcInstance npcInstance){
        if(!npcInstanceMap.containsViewer(npcUuid, player.getUniqueId())) return;

        new SpawnPackets().despawnPacket(player, npcInstance);

        npcInstanceMap.removeViewer(npcUuid, player.getUniqueId());
    }

}
