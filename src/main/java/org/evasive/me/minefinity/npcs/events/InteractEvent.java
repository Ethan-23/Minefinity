package org.evasive.me.minefinity.npcs.events;

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
import org.evasive.me.minefinity.npcs.NpcInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.evasive.me.minefinity.Minefinity.npcInstanceMap;

public class InteractEvent extends PacketListenerAbstract {

    private final Map<UUID, Long> lastInteractionTime = new HashMap<>();

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

        org.bukkit.Bukkit.getScheduler().runTask(Minefinity.getCore(), () -> npc.getType().createBehavior().onInteract(player));

    }

    private void adminInteract(Player player, NpcInstance npc) {
        player.sendMessage("Removed Npc: " + npc.getType().name() + " With UUID: " + npc.getNpcUuid());
        player.sendMessage("NPC STAND: " + npc.getStand().toString());
        Bukkit.getScheduler().runTask(Minefinity.getCore(), () -> npc.getStand().remove());


    }
}
