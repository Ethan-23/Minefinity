package org.evasive.me.minefinity.core.npcs.events;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.npcs.NpcBehaviorRegistry;
import org.evasive.me.minefinity.core.npcs.NpcInstance;
import org.evasive.me.minefinity.core.npcs.NpcInstanceMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractEvent extends PacketListenerAbstract {

    private final Map<UUID, Long> lastInteractionTime = new HashMap<>();
    private final NpcInstanceMap npcInstanceMap;
    private final NpcBehaviorRegistry npcBehaviorRegistry;

    public InteractEvent(NpcInstanceMap npcInstanceMap, NpcBehaviorRegistry npcBehaviorRegistry) {
        this.npcInstanceMap = npcInstanceMap;
        this.npcBehaviorRegistry = npcBehaviorRegistry;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        WrapperPlayClientInteractEntity entityInteract = new WrapperPlayClientInteractEntity(event);
        int clickedEntityId = entityInteract.getEntityId();

        if(entityInteract.getHand() != InteractionHand.MAIN_HAND)
            return;

        Player player = event.getPlayer();

        NpcInstance npc = npcInstanceMap.getNpc(clickedEntityId);
        if(npc == null)
            return;

        if(player.getGameMode() == GameMode.CREATIVE && player.isSneaking() && player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD){
            adminInteract(player, npc);
        }else{
            playerInteract(entityInteract, player, npc);
        }

        long currentTime = System.currentTimeMillis();

        // Cooldown duration in milliseconds
        long cooldownMillis = 300;
        if (lastInteractionTime.containsKey(player.getUniqueId()) &&
                currentTime - lastInteractionTime.get(player.getUniqueId()) < cooldownMillis) {
            return;
        }

        lastInteractionTime.put(player.getUniqueId(), currentTime);

    }

    private void playerInteract(WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity, Player player, NpcInstance npc) {
        if (wrapperPlayClientInteractEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.INTERACT)
            return;

        org.bukkit.Bukkit.getScheduler().runTask(Minefinity.getCore(), () -> npcBehaviorRegistry.getBehavior(npc.getType().name().toLowerCase()).get().onInteract(player));

    }

    private void adminInteract(Player player, NpcInstance npc) {
        player.sendMessage("Removed Npc: " + npc.getType().name() + " With UUID: " + npc.getNpcUuid());
        player.sendMessage("NPC STAND: " + npc.getStand().toString());
        Bukkit.getScheduler().runTask(Minefinity.getCore(), () -> npc.getStand().remove());
        npcInstanceMap.removeNpc(npc.getNpcUuid());
    }
}
