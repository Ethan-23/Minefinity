package org.evasive.me.minevolutionCore.arcanecrafting;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import java.util.UUID;

public class MatrixRightClickEvents implements Listener {

    MatrixManager matrixManager = new MatrixManager();

    @EventHandler
    public void rightClickCryingObsidian(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if(e.getAction().isLeftClick())
            return;

        if(e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        if(block == null || block.getType() != Material.CRYING_OBSIDIAN)
            return;

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() == Material.AIR && !matrixManager.hasItem(player.getUniqueId())){
            //Return items on display
            return;
        }

        e.setCancelled(true);

        if(matrixManager.hasItem(player.getUniqueId())){
            player.sendMessage("Item Retrieved");
            player.getInventory().addItem(matrixManager.getItem(player.getUniqueId()));
            matrixManager.removePlayer(player.getUniqueId());
            return;
        }

        player.sendMessage("Item Applied");

        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        matrixManager.addItem(player.getUniqueId(), item, e.getClickedBlock().getLocation().add(0.5f, 2, 0.5f), player);



    }

    @EventHandler
    public void rightClickPedestal(PlayerInteractEvent e){
        /*Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if(e.getAction().isLeftClick())
            return;

        if(e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        if(block == null || (block.getType() != Material.POLISHED_BLACKSTONE_BRICK_STAIRS && block.getType() != Material.POLISHED_BLACKSTONE_BRICKS))
            return;

        player.sendMessage("Pedestal");

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() == Material.AIR){
            //Return items on display
            return;
        }


        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

        ItemDisplay itemDisplay = block.getWorld().spawn(block.getLocation().add(0.5f, 1.25, 0.5f), ItemDisplay.class);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(0.5D);
        itemDisplay.setItemStack(item);
        itemDisplay.setTransformation(transformation);
        player.sendMessage("Amount of items: "+itemDisplay.getItemStack().getAmount());
        e.setCancelled(true);*/
    }

}
