package org.evasive.me.minevolutionCore.enchanting.runicMatrix.events;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.enchanting.runicMatrix.MatrixManager;

public class EnchantInteractEvent implements Listener {

    final MatrixManager matrixManager = MinevolutionCore.getMatrixManager();

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Interaction interaction) || event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player player = event.getPlayer();
        addEnchant(interaction, player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Interaction interaction && event.getDamager() instanceof Player player) {
            addEnchant(interaction, player);
            event.setCancelled(true);
        }
    }

    public void addEnchant(Interaction interaction, Player player){
        NamespacedKey namespacedKey = new NamespacedKey(MinevolutionCore.getCore(), "enchantingID");
        int clickedEnchantId = interaction.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
        matrixManager.increaseTier(player);
        matrixManager.applyEnchant(player, clickedEnchantId);
    }

}
