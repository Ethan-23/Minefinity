package org.evasive.me.minefinity.towns.structures.forge.blacksmith.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.customItems.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.ForgeRecipeManager;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.service.ForgeHandler;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.service.ForgeService;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.*;
import static org.evasive.me.minefinity.core.utils.TimeCalculator.getString;

public class ForgeGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int INFORMATION_SLOT = 49;
    public static final List<Integer> FORGE_SLOTS = List.of(11, 12, 13, 14, 15);
    private static final List<Integer> FORGE_PANES = List.of(20, 21, 22, 23, 24);
    private final CustomItemRegistryService customItemRegistryService;
    private final ForgeService forgeService;
    private final RecipeService recipeService;
    private final ForgeRecipeManager forgeRecipeManager;

    public ForgeGui(Player player, CustomItemRegistryService customItemRegistryService, ForgeRecipeManager forgeRecipeManager, ForgeService forgeService, RecipeService recipeService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Forge"));
        this.forgeRecipeManager = forgeRecipeManager;
        this.forgeService = forgeService;
        this.customItemRegistryService = customItemRegistryService;
        this.recipeService = recipeService;
        build();
    }

    @Override
    protected void build() {
        buildFrame();
        buildProgressPanes(player);
        buildForgeSlots(player);
        buildInformationSlot();
        createRefresh(player);
    }

    private void buildFrame(){
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inventory.setItem(i, fillerPane);
        }
    }

    private void buildInformationSlot(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.KNOWLEDGE_BOOK, TextConversions.parse("<bold><gold>Miner"));
        itemBuilder.addLore("<gray>This is the miner. He be mining the blocks and stuff. He be mining all night and day. Might be a 'worker' if you catch my drift. 3 Netherite Ingots!");
        inventory.setItem(INFORMATION_SLOT, itemBuilder.build());
    }

    private void buildForgeSlots(Player player){
        for(int i = 0; i < FORGE_SLOTS.size(); i++){
            inventory.setItem(FORGE_SLOTS.get(i), buildForgeButton(player, i+1));
        }
    }

    private void buildProgressPanes(Player player){

        for(int i = 0; i < FORGE_PANES.size(); i++){
            int forgeId = i+1;

            if(!forgeService.hasForgeItem(player, forgeId)) {
                inventory.setItem(FORGE_PANES.get(i), emptyPane);
                continue;
            }

            long milliseconds = forgeService.getMilisecondsRemaining(player, forgeId);
            if(forgeService.isFinished(player, forgeId)){
                inventory.setItem(FORGE_PANES.get(i), donePane);
            } else {
                ItemBuilder progressPaneBuilder = new ItemBuilder(progressPane.clone());
                progressPaneBuilder.addLore("<gold>" + getString(milliseconds));
                inventory.setItem(FORGE_PANES.get(i), progressPaneBuilder.build());
            }
        }
    }

    public ItemStack buildForgeButton(Player player, int forgeNum){
        if(forgeService.hasForgeItem(player, forgeNum)){
            ItemBuilder pendingForge = new ItemBuilder(forgeService.getForgeItemStack(player, forgeNum));
            if(!forgeService.isFinished(player, forgeNum))
                pendingForge.addLore("<gold>Time Remaining: <yellow>" + getString(forgeService.getMilisecondsRemaining(player, forgeNum)));
            return pendingForge.build();

        }else{
            ItemBuilder forgeButton = new ItemBuilder(Material.FURNACE, TextConversions.parse("<yellow>Empty Forge Slot</yellow>"));
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
        new ForgeHandler(customItemRegistryService, forgeRecipeManager, recipeService, forgeService).handleMainForge(player, e.getSlot());
    }
}
