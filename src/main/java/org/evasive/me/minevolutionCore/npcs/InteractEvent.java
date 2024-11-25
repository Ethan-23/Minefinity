package org.evasive.me.minevolutionCore.npcs;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.blocks.gui.BlockGUI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractEvent extends PacketListenerAbstract {

    private final Map<UUID, Long> lastInteractionTime = new HashMap<>();
    private final long cooldownMillis = 300; // Cooldown duration in milliseconds

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity entityInteract = new WrapperPlayClientInteractEntity(event);
            int clickedEntityId = entityInteract.getEntityId();

            if(entityInteract.getAction() != WrapperPlayClientInteractEntity.InteractAction.INTERACT || entityInteract.getHand() != InteractionHand.MAIN_HAND)
                return;

            Player player = event.getPlayer();
            NPC npc = MinevolutionCore.getNpcManager().getNpcTracker().getNPC(player.getUniqueId(), clickedEntityId);

            if(npc == null)
                return;

            if(clickedEntityId != npc.getEntityID())
                return;

            long currentTime = System.currentTimeMillis();

            if (lastInteractionTime.containsKey(player.getUniqueId()) &&
                    currentTime - lastInteractionTime.get(player.getUniqueId()) < cooldownMillis) {
                return;
            }

            lastInteractionTime.put(player.getUniqueId(), currentTime);

            if(npc.getName().equals("BlockMaster")){
                //Check if player is in the tutorial
                blockMasterResponse(player);
            }

            //player.sendMessage("NPC right-clicked: " + npc.getName());

        }
    }

    public void blockMasterResponse(Player player){
        Bukkit.getScheduler().runTask(MinevolutionCore.getCore(), () -> new BlockGUI().openInventory(player));
    }

}
