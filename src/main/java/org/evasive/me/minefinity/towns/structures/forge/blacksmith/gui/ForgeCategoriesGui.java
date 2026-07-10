package org.evasive.me.minefinity.towns.structures.forge.blacksmith.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.core.utils.TimeCalculator;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.RecipeRequirement;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.ForgeCategories;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.BaseForgeRecipe;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.ForgeRecipeManager;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.service.ForgeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.*;

public class ForgeCategoriesGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    public static final int MATERIAL_CATEGORY_SLOT = 38;
    public static final int COMPONENT_CATEGORY_SLOT = 39;
    public static final int PICKAXE_CATEGORY_SLOT = 40;
    public static final int STORAGE_CATEGORY_SLOT = 41;
    public static final int BACK_SLOT = 45;
    public static final int EXIT_SLOT = 49;
    public static final List<Integer> RECIPE_SLOTS = List.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
    private final ForgeCategories forgeCategory;
    private final ForgeRecipeManager forgeRecipeManager;
    private final CustomItemRegistryService customItemRegistryService;
    private final RecipeService recipeService;
    private final ForgeService forgeService;

    public ForgeCategoriesGui(Player player, CustomItemRegistryService customItemRegistryService, ForgeCategories category, ForgeRecipeManager forgeRecipeManager, ForgeService forgeService, RecipeService recipeService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Forge Categories"));
        this.forgeCategory = category;
        this.forgeRecipeManager = forgeRecipeManager;
        this.customItemRegistryService = customItemRegistryService;
        this.recipeService = recipeService;
        this.forgeService = forgeService;
        build();
    }

    //Maybe cache Map<ForgeCategories, List<ForgeRecipes>> in the future?

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        buildOptions();
        buildRecipes();
    }

    /**
     * Builds the frame of the gui
     */
    private void buildOptions(){
        inventory.setItem(MATERIAL_CATEGORY_SLOT, buildCategory(ForgeCategories.PICKAXES, this.forgeCategory));
        inventory.setItem(COMPONENT_CATEGORY_SLOT, buildCategory(ForgeCategories.TOOL_HEADS, this.forgeCategory));
        inventory.setItem(PICKAXE_CATEGORY_SLOT, buildCategory(ForgeCategories.TOOL_CORES, this.forgeCategory));
        inventory.setItem(STORAGE_CATEGORY_SLOT, buildCategory(ForgeCategories.TOOL_HANDLES, this.forgeCategory));
        inventory.setItem(BACK_SLOT, backPage);
        inventory.setItem(EXIT_SLOT, exit);

        for(int recipeSlot : RECIPE_SLOTS){
            inventory.setItem(recipeSlot, mysteryPane);
        }
    }

    /**
     * Creates ItemStacks for each category.
     * @param category Category being created.
     * @param selectedCategory Currently selected category.
     * @return ItemStack for selectable or selected category.
     */
    private ItemStack buildCategory(ForgeCategories category, ForgeCategories selectedCategory){
        CustomItemBuilder builder = category.toItemBuilder();
        if(category == selectedCategory){
            setSelected(builder);
        }
        return builder.build();
    }

    /**
     * Fills in the recipe slots with available recipes.
     */
    private void buildRecipes(){

        List<BaseForgeRecipe> categoryList = forgeRecipeManager.getRecipes().values().stream()
                .filter(r -> r.getForgeCategory() == this.forgeCategory)
                .toList();

        for(int slot = 0; slot < RECIPE_SLOTS.size(); slot++){

            if(slot >= categoryList.size()) break;

            BaseForgeRecipe currentRecipe = categoryList.get(slot);
            String itemId = currentRecipe.getResult();

            CustomItemBuilder forgeItem = new CustomItemBuilder(customItemRegistryService.getRegisteredItemStack(itemId));

            //Change to check if player just has recipe in set of recipes when I add that <<<<<<
            if(!recipeService.hasRecipeUnlocked(player.getUniqueId(), currentRecipe.getResult())){
                forgeItem.setMaterial(Material.BARRIER);
                forgeItem.setDisplayName(TextConversions.formatItemName(itemId) + " <red>(Locked)");
                forgeItem.setLore(new ArrayList<>());
                forgeItem.addBlank();
                forgeItem.addLore("<gold>Requirements:");
                for (RecipeRequirement recipeRequirement : currentRecipe.getRequirements()) {
                    String color = recipeService.checkRecipeRequirement(player, recipeRequirement) ? "<strikethrough><green>" : "<red>";
                    forgeItem.addLore(color + recipeRequirement.getDisplay());
                }
            } else {
                forgeItem.addBlank().addLore("<bold><gold>Recipe:");

                for (Map.Entry<String, Integer> entry : currentRecipe.getRecipe().entrySet()) {

                    BaseCustomItem customItem = customItemRegistryService.getRegisteredBaseItem(entry.getKey());

                    if(customItem != null){

                        int amount = entry.getValue();
                        String name = customItem.getDisplayName();
                        forgeItem.addLore(TextConversions.setRarityColor(name, customItem.getRarity()) + "<white> x" + amount).build();
                    }else {
                        forgeItem.addLore("<red>Unknown Item <bold>" +  entry.getKey());
                    }
                }

                forgeItem.addBlank().addLore("<bold><gold>Forge Time:" );
                forgeItem.addLore("<yellow>" + (TimeCalculator.getString(currentRecipe.getCraftTime() * 1000L)));
            }

            inventory.setItem(RECIPE_SLOTS.get(slot), forgeItem.build());

        }
    }

    /**
     * Updated selected CustomItemBuilder to glow and add SELECTED lore
     * @param builder CustomItemBuilder being changed.
     */
    public void setSelected(CustomItemBuilder builder){
        builder.addLore("<bold><green>SELECTED");
    }


    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        Player player = (Player) e.getWhoClicked();
        int selectedSlot = e.getSlot();

        e.setCancelled(true);

        if(ForgeCategoriesGui.RECIPE_SLOTS.contains(selectedSlot)){

            ItemStack currentItem = e.getCurrentItem();
            if(currentItem == null || currentItem.isEmpty())
                return;

            String itemId = customItemRegistryService.getItemId(currentItem);

            if(itemId == null)
                return;

            BaseForgeRecipe itemRecipe = forgeRecipeManager.getRecipe(itemId);

            if(!forgeService.hasRecipeUnlocked(player, itemRecipe))
                return;

            new ForgeConfirmationGui(player, customItemRegistryService, forgeRecipeManager.getRecipe(itemId), forgeRecipeManager, recipeService, forgeService).openInventory(player);
        }

        if(selectedSlot >= ForgeCategoriesGui.MATERIAL_CATEGORY_SLOT && selectedSlot <= ForgeCategoriesGui.STORAGE_CATEGORY_SLOT){
            int categoryOrdinal = selectedSlot - 38; // Find a better way?
            new ForgeCategoriesGui(player, customItemRegistryService, ForgeCategories.values()[categoryOrdinal], forgeRecipeManager, forgeService, recipeService).open();
        }

        if(selectedSlot == ForgeCategoriesGui.BACK_SLOT){
            new ForgeGui(player, customItemRegistryService, forgeRecipeManager, forgeService, recipeService).open();
        }

        if(selectedSlot == ForgeCategoriesGui.EXIT_SLOT){
            player.closeInventory();
        }
    }
}
