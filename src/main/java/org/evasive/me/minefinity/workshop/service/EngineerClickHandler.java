package org.evasive.me.minefinity.workshop.service;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.utils.TextConversions;
import org.evasive.me.minefinity.workshop.WorkshopMode;
import org.evasive.me.minefinity.workshop.gui.EngineerGui;
import org.evasive.me.minefinity.workshop.recipes.BaseWorkshopRecipe;
import org.evasive.me.minefinity.workshop.recipes.WorkshopRecipeManager;
import org.evasive.me.minefinity.workshop.tools.WorkshopToolsTiers;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.hasItemId;
import static org.evasive.me.minefinity.utils.guis.SwapItems.swapCursor;
import static org.evasive.me.minefinity.workshop.gui.EngineerGui.*;

public class EngineerClickHandler {

    private final Player player;
    private final WorkshopMode workshopMode;
    private final EngineerService engineerService;
    private final EngineerCraftingService engineerCraftingService;
    private final RecipeService recipeService;
    private final WorkshopRecipeManager workshopRecipeManager;

    private static final String TOOL_CRAFT_FAILED = "<red>You need to craft a tool.";
    private static final String INCORRECT_RESOURCE_AMOUNT = "<red>You do not have the correct amount of resources.";

    public EngineerClickHandler(Player player, WorkshopMode workshopMode, EngineerService engineerService, WorkshopRecipeManager workshopRecipeManager) {
        this.player = player;
        this.workshopMode = workshopMode;
        this.engineerService = engineerService;
        this.workshopRecipeManager = workshopRecipeManager;
        recipeService = new RecipeService();
        this.engineerCraftingService = new EngineerCraftingService(engineerService,  player, workshopMode);
    }

    public void handleEngineerClick(InventoryClickEvent e){
        int slot = e.getSlot();

        if(slot == SWAP_SLOT){
            handleSwapSlot();
            return;
        }

        if(slot == RESOURCE_SLOT && e.getClick().isLeftClick()){
            handleResourceSlot(e);
            return;
        }

        if(SHOP_SLOTS.contains(slot)){
            handleShopSlots(e);
            return;
        }

        if(slot == TOOL_SLOT && e.getClick() == ClickType.SHIFT_RIGHT){
            handleToolSlots();
        }
    }

    private void handleSwapSlot(){
        new EngineerGui(player, this.workshopMode == WorkshopMode.CARPENTRY ? WorkshopMode.STONEWORKING : WorkshopMode.CARPENTRY, this.engineerService, workshopRecipeManager).open();
    }

    private void handleResourceSlot(InventoryClickEvent e){
        ItemStack cursorItem = e.getCursor();
        WorkshopToolsTiers currentWorkshopResource = engineerService.getWorkshopCurrentResource(player, workshopMode);
        WorkshopToolsTiers replacementWorkshopResource = cursorItem.isEmpty() || !hasItemId(cursorItem) || !WorkshopToolsTiers.contains(getItemId(cursorItem)) ? null : WorkshopToolsTiers.valueOf(getItemId(cursorItem));
        boolean noResource = currentWorkshopResource == null;
        if(noResource && replacementWorkshopResource == null)
            return;

        if(replacementWorkshopResource == null || currentWorkshopResource != replacementWorkshopResource){
            swapCursor(e, noResource ? null : CustomItemRegistry.getByID(engineerService.getWorkshopCurrentResource(player, workshopMode).name()), noResource ? 0 : engineerService.getWorkshopCurrentResourceCount(player, workshopMode));
            engineerService.setWorkshopCurrentResource(player, workshopMode, replacementWorkshopResource);
            engineerService.setWorkshopCurrentResourceCount(player, workshopMode, cursorItem.getAmount());
        } else {
            int currentAmount = engineerService.getWorkshopCurrentResourceCount(player, workshopMode);
            int addingAmount = e.getCursor().getAmount();
            cursorItem.setAmount(Math.max(currentAmount + addingAmount - 64, 0));
            engineerService.setWorkshopCurrentResourceCount(player, workshopMode, Math.min(currentAmount + addingAmount, 64));

        }

        if(engineerService.getWorkshopToolType(player, workshopMode) == null && engineerCraftingService.canCraftNextTool())
            engineerCraftingService.craftTool();
    }

    private void handleShopSlots(InventoryClickEvent e){
        ItemStack currentItem = e.getCurrentItem();
        if(!hasItemId(currentItem)) return;
        BaseWorkshopRecipe workshopRecipe = workshopRecipeManager.getRecipe(getItemId(currentItem));

        int toolDurability = engineerService.getWorkshopToolDurability(player, workshopMode);

        if(toolDurability <= 0){
            player.sendMessage(TextConversions.parse(TOOL_CRAFT_FAILED));
            return;
        }

        if(engineerService.getWorkshopToolDurability(player, workshopMode) < toolDurability) return;

        boolean purchased = handlePurchase(player, workshopRecipe);

        if(!purchased) return;

        engineerService.setWorkshopToolDurability(player, workshopMode, toolDurability - workshopRecipe.getDurabilityUsage());

        if(toolDurability - workshopRecipe.getDurabilityUsage() == 0 && engineerCraftingService.canCraftNextTool()){
            engineerCraftingService.craftTool();
        }else if (toolDurability - workshopRecipe.getDurabilityUsage() <= 0){
            engineerService.setWorkshopToolType(player, workshopMode, null);
        }
    }

    private void handleToolSlots(){
        this.engineerService.setWorkshopToolDurability(player, workshopMode, 0);
        this.engineerService.setWorkshopToolType(player, workshopMode, null);
        if(engineerCraftingService.canCraftNextTool())
            engineerCraftingService.craftTool();
    }

    public boolean handlePurchase(Player player, BaseWorkshopRecipe workshopRecipe){

        boolean canPurchase = recipeService.tryPurchaseItem(player, workshopRecipe);

        if(!canPurchase)player.sendMessage(TextConversions.parse(INCORRECT_RESOURCE_AMOUNT));
        return canPurchase;
    }

}
