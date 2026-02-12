package org.evasive.me.minefinity.forge.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.forge.data.ForgeManager;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.evasive.me.minefinity.utils.GenericGuiItems.*;
import static org.evasive.me.minefinity.utils.TimeCalculator.getString;

public class ForgeGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final List<Integer> FUEL_SLOTS = List.of(47, 48, 49, 50, 51);
    public static final List<Integer> FORGE_SLOTS = List.of(20, 21, 22, 23, 24);
    private static final Map<Integer, Set<Integer>> FORGE_PANES = Map.of(
            1, Set.of(2, 11, 29, 38),
            2, Set.of(3, 12, 30, 39),
            3, Set.of(4, 13, 31, 40),
            4, Set.of(5, 14, 32, 41),
            5, Set.of(6, 15, 33, 42)
    );
    ForgeManager forgeManager;

    public ForgeGui(Player player) {
        super(player, INVENTORY_SIZE, Messages.parse("Forge"));
        build();
    }

    @Override
    protected void build() {
        forgeManager = new ForgeManager();
        buildFrame();
        buildProgressPanes(player);
        buildForgeSlots(player);
        buildFuelSlots();
        createRefresh(player);
    }

    private void buildFrame(){
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inventory.setItem(i, fillerPane);
        }
    }

    private void buildFuelSlots(){
        for (int fuelSlot : FUEL_SLOTS) {
            inventory.setItem(fuelSlot, new ItemStack(Material.GRAY_DYE));
        }
    }

    private void buildForgeSlots(Player player){
        for(int i = 0; i < FORGE_SLOTS.size(); i++){
            inventory.setItem(FORGE_SLOTS.get(i), buildForgeButton(player, i+1));
        }
    }

    private void buildProgressPanes(Player player){

        for (Map.Entry<Integer, Set<Integer>> entry : FORGE_PANES.entrySet()) {
            int key = entry.getKey();
            Set<Integer> value = entry.getValue();

            if(!forgeManager.hasForgeItem(player, key)){
                for (int slot : value)
                    inventory.setItem(slot, emptyPane);
                continue;
            }

            long milliseconds = forgeManager.getMilisecondsRemaining(player, key);

            if(forgeManager.isFinished(player, key)){
                for (int slot : value)
                    inventory.setItem(slot, donePane);
            } else {
                ItemBuilder progressPaneBuilder = new ItemBuilder(progressPane.clone());
                progressPaneBuilder.addLore("<gold>" + getString(milliseconds));
                for (int slot : value)
                    inventory.setItem(slot, progressPaneBuilder.build());
            }
        }
    }

    public ItemStack buildForgeButton(Player player, int forgeNum){
        if(forgeManager.hasForgeItem(player, forgeNum)){
            ItemBuilder pendingForge = new ItemBuilder(forgeManager.getForgeItemStack(player, forgeNum));
            if(!forgeManager.isFinished(player, forgeNum))
                pendingForge.addLore("<gold>Time Remaining: <yellow>" + getString(forgeManager.getMilisecondsRemaining(player, forgeNum)));
            return pendingForge.build();

        }else{
            ItemBuilder forgeButton = new ItemBuilder(Material.FURNACE, Messages.parse("<yellow>Empty Forge Slot</yellow>"));
            forgeButton.addLore("<gray>Click to select a forge recipe");
            return forgeButton.build();
        }
    }

    public void createRefresh(Player player){
        Bukkit.getScheduler().runTaskLater(Minefinity.getCore(), () -> {
            if(player.getOpenInventory().getTopInventory().getHolder() instanceof ForgeGui)
                build();
        }, 20).getTaskId();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);
        new ForgeHandler().handleMainForge(player, e.getSlot());
    }
}
