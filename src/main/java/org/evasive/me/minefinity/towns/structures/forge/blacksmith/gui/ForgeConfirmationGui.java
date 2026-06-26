package org.evasive.me.minefinity.towns.structures.forge.blacksmith.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.core.utils.TimeCalculator;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.ForgeCategories;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.BaseForgeRecipe;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.ForgeRecipeManager;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.service.ForgeHandler;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.service.ForgeService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.*;

public class ForgeConfirmationGui extends BaseGui {

    public static final List<Integer> RECIPE_SLOTS = List.of(10, 11, 19, 20, 28, 29);
    public static final int RESULT_PREVIEW_SLOT = 16;
    public static final int START_BUTTON_SLOT = 22;
    public static final int BACK_BUTTON_SLOT = 45;
    public static final int EXIT_BUTTON_SLOT = 49;
    private static final int INVENTORY_SIZE = 54;
    private static final int STACK_SIZE = 64;
    private final BaseForgeRecipe crafting;
    private final ForgeRecipeManager forgeRecipeManager;
    private final CustomItemRegistryService customItemRegistryService;
    private final RecipeService recipeService;
    private final ForgeService forgeService;
    ForgeHandler forgeHandler;


    public ForgeConfirmationGui(Player player, CustomItemRegistryService customItemRegistryService, BaseForgeRecipe crafting, ForgeRecipeManager forgeRecipeManager, RecipeService recipeService, ForgeService forgeService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Forge Confirmation"));
        this.crafting = crafting;
        this.forgeRecipeManager = forgeRecipeManager;
        this.customItemRegistryService = customItemRegistryService;
        this.recipeService = recipeService;
        this.forgeService = forgeService;
        forgeHandler = new ForgeHandler(customItemRegistryService, forgeRecipeManager, recipeService, forgeService);
        build();
    }

    @Override
    protected void build() {
        buildFrame();
        buildResult();
        buildButtons();
        buildRecipePreview();
    }


    private void buildFrame(){
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inventory.setItem(i, fillerPane);
        }
    }

    private void buildResult(){
        inventory.setItem(RESULT_PREVIEW_SLOT, customItemRegistryService.getRegisteredItemStack(crafting.getResult()));
    }

    private void buildButtons(){
        inventory.setItem(START_BUTTON_SLOT, buildStartButton());
        inventory.setItem(BACK_BUTTON_SLOT, backPage);
        inventory.setItem(EXIT_BUTTON_SLOT, exit);
    }

    private ItemStack buildStartButton() {
        return new ItemBuilder(Material.FURNACE, TextConversions.parse("<bold><green>START")).addBlank()
                .addLore("<gold>Forge Time: <yellow>" + TimeCalculator.getString(crafting.getCraftTime() * 1000L))
                .build();
    }

    private void buildRecipePreview(){
        int slotIndex = 0;
        for (Map.Entry<String, Integer> entry : crafting.getRecipe().entrySet()) {
            if (slotIndex >= RECIPE_SLOTS.size()) break;

            ItemStack item = customItemRegistryService.getRegisteredItemStack(entry.getKey());
            int amount = entry.getValue();

            while (amount > 0) {
                int stackAmount = Math.min(amount, STACK_SIZE);
                item.setAmount(stackAmount);
                inventory.setItem(RECIPE_SLOTS.get(slotIndex), item.clone()); // clone to avoid overwriting
                amount -= stackAmount;
                slotIndex++;
                if (slotIndex >= RECIPE_SLOTS.size()) break;
            }
        }

        for (; slotIndex < RECIPE_SLOTS.size(); slotIndex++) {
            inventory.setItem(RECIPE_SLOTS.get(slotIndex), blankOrange);
        }
    }

    public void openInventory(Player player){
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);
        ItemStack result = Objects.requireNonNull(e.getClickedInventory()).getItem(ForgeConfirmationGui.RESULT_PREVIEW_SLOT);

        int clickedSlot = e.getSlot();

        if(clickedSlot == ForgeConfirmationGui.START_BUTTON_SLOT) {
            forgeHandler.startForgeAttempt(player, result);
        }

        if (clickedSlot == ForgeConfirmationGui.BACK_BUTTON_SLOT) {
            new ForgeCategoriesGui(player, customItemRegistryService, ForgeCategories.PICKAXE_TEMPLATES, forgeRecipeManager, forgeService, recipeService).open();
        }

        if (clickedSlot == ForgeConfirmationGui.EXIT_BUTTON_SLOT) {
            player.closeInventory();
        }
    }
}
