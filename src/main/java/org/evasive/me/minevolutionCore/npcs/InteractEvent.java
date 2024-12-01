package org.evasive.me.minevolutionCore.npcs;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.blocks.gui.BlockGUI;
import org.evasive.me.minevolutionCore.enchanting.runicMatrix.MatrixManager;
import org.evasive.me.minevolutionCore.forge.gui.ForgeGUIMain;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;
import org.evasive.me.minevolutionCore.utils.PickaxeKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractEvent extends PacketListenerAbstract {

    private final Map<UUID, Long> lastInteractionTime = new HashMap<>();

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

            // Cooldown duration in milliseconds
            long cooldownMillis = 300;
            if (lastInteractionTime.containsKey(player.getUniqueId()) &&
                    currentTime - lastInteractionTime.get(player.getUniqueId()) < cooldownMillis) {
                return;
            }

            lastInteractionTime.put(player.getUniqueId(), currentTime);

            if(npc.getName().equals("BlockMaster")){
                //Check if player is in the tutorial
                blockMasterResponse(player);
            }

            if(npc.getName().equals("Archmage")){
                archmageResponse(player);
            }

            if(npc.getName().equals("Blacksmith")){
                blacksmithResponse(player);
            }

            //player.sendMessage("NPC right-clicked: " + npc.getName());

        }
    }

    public void blockMasterResponse(Player player){
        Bukkit.getScheduler().runTask(MinevolutionCore.getCore(), () -> new BlockGUI().openInventory(player));
    }

    public void archmageResponse(Player player){
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.getType() == Material.AIR){
            player.sendMessage("Archmage: Bring me your pickaxe once you complete your block quest");
            return;
        }
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(PickaxeKeys.holdingPickaxeKey))
            return;
        int progress = itemStack.getItemMeta().getPersistentDataContainer().get(PickaxeKeys.tierBlocksKey, PersistentDataType.INTEGER);
        int cap = itemStack.getItemMeta().getPersistentDataContainer().get(PickaxeKeys.tierBlocksCapKey, PersistentDataType.INTEGER);
        if(progress < cap){
            player.sendMessage("Archmage: This pickaxe is not ready to upgrade");
        }else{
            // Create "Yes" button
            Component yesButton = Component.text("[Yes]")
                    .color(NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text("Click to agree")))
                    .clickEvent(ClickEvent.runCommand("/enchant_action_yes"));

            Component noButton = Component.text("[No]")
                    .color(NamedTextColor.RED)
                    .hoverEvent(HoverEvent.showText(Component.text("Click to disagree")))
                    .clickEvent(ClickEvent.runCommand("/enchant_action_no"));

            player.sendMessage(ComponentUtils.makeText("Archmage: Do you wish to enchant your pickaxe? ", NamedTextColor.WHITE, false).append(yesButton).append(Component.text(" ")).append(noButton));
        }
    }

    final MatrixManager matrixManager = MinevolutionCore.getMatrixManager();

    public void addEnchantingItem(Player player, Location location){
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() == Material.AIR || !item.getItemMeta().getPersistentDataContainer().has(PickaxeKeys.holdingPickaxeKey)){
            player.sendMessage("Archmage: I cant seem to find your pickaxe anymore");
            return;
        }
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        matrixManager.addItem(player.getUniqueId(), item, location.add(0f, 2, 0f), player);
        player.sendMessage("Archmage: Choose an enchant you wish to attempt to apply to your pickaxe");
    }

    public void handleNo(Player player){
        player.sendMessage("Archmage: Let me know if you would like to enchant your pickaxe in the future");
    }

    public void handleYes(Player player){
        addEnchantingItem(player, new Location(player.getWorld(), 14.5f, 1f, -13.5f));
    }

    public void blacksmithResponse(Player player){
        Bukkit.getScheduler().runTask(MinevolutionCore.getCore(), () -> new ForgeGUIMain().openInventory(player));
    }

}
