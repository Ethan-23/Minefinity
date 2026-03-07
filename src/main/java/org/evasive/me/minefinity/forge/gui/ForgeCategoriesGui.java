package org.evasive.me.minefinity.forge.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.forge.data.ForgeCategories;
import org.evasive.me.minefinity.forge.recipes.BaseForgeRecipe;
import org.evasive.me.minefinity.forge.recipes.ForgeRecipes;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;
import org.evasive.me.minefinity.utils.TimeCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.hasItemId;
import static org.evasive.me.minefinity.utils.GenericGuiItems.*;
import static org.evasive.me.minefinity.utils.TextConversions.formatItemName;

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

    public ForgeCategoriesGui(Player player, ForgeCategories category) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Forge Categories"));
        this.forgeCategory = category;
        build();
    }

    //Maybe cache Map<ForgeCategories, List<ForgeRecipes>> in the future?

    @Override
    protected void build() {
        buildFrame();
        buildRecipes();
    }

    /**
     * Builds the frame of the gui
     */
    private void buildFrame(){
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            switch (i){
                case MATERIAL_CATEGORY_SLOT -> inventory.setItem(i, buildCategory(ForgeCategories.PICKAXE_TEMPLATES, this.forgeCategory));
                case COMPONENT_CATEGORY_SLOT -> inventory.setItem(i, buildCategory(ForgeCategories.PICKAXE_HEADS, this.forgeCategory));
                case PICKAXE_CATEGORY_SLOT -> inventory.setItem(i, buildCategory(ForgeCategories.PICKAXE_CORES, this.forgeCategory));
                case STORAGE_CATEGORY_SLOT -> inventory.setItem(i, buildCategory(ForgeCategories.PICKAXE_HANDLES, this.forgeCategory));
                case BACK_SLOT -> inventory.setItem(i, backPage);
                case EXIT_SLOT -> inventory.setItem(i, exit);
                default -> inventory.setItem(i, fillerPane);
            }
        }
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
        ItemBuilder builder = category.toItemBuilder();
        if(category == selectedCategory){
            setSelected(builder);
        }
        return builder.build();
    }

    /**
     * Fills in the recipe slots with available recipes.
     */
    private void buildRecipes(){
        List<ForgeRecipes> categoryList = Arrays.stream(ForgeRecipes.values())
                .filter(r -> r.getBaseForgeRecipe().getForgeCategory() == this.forgeCategory)
                .toList();

        for(int slot = 0; slot < RECIPE_SLOTS.size(); slot++){

            if(slot >= categoryList.size()) break;

            BaseForgeRecipe forgeCrafting = categoryList.get(slot).getBaseForgeRecipe();

            ItemBuilder forgeItem = new ItemBuilder(forgeCrafting.getResult().getBaseItem().buildItem().clone());
            forgeItem.addBlank().addLore("<bold><gold>Recipe:");

            for (Map.Entry<CustomItem, Integer> entry : forgeCrafting.getRecipe().entrySet()) {
                BaseCustomItem customItem = (BaseCustomItem) entry.getKey();
                ItemStack recipeItem = customItem.getBaseItem().buildItem();
                int amount = entry.getValue();
                String name = hasItemId(recipeItem)
                        ? formatItemName(getItemId(recipeItem))
                        : recipeItem.getType().name();
                forgeItem.addLore("<"+customItem.getRarity().getRarityBuilder().getTextColor().asHexString()+">" + name + "<white> x" + amount).build();
            }

            forgeItem.addBlank().addLore("<bold><gold>Forge Time:" );
            forgeItem.addLore("<yellow>" + (TimeCalculator.getString(forgeCrafting.getCraftTime() * 1000L)));

            inventory.setItem(RECIPE_SLOTS.get(slot), forgeItem.build());

        }
    }

    /**
     * Updated selected ItemBuilder to glow and add SELECTED lore
     * @param builder ItemBuilder being changed.
     */
    public void setSelected(ItemBuilder builder){
        builder.addLore("<bold><green>SELECTED");
    }


    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        Player player = (Player) e.getWhoClicked();
        int selectedSlot = e.getSlot();

        e.setCancelled(true);

        if(ForgeCategoriesGui.RECIPE_SLOTS.contains(selectedSlot)){

            if(!Objects.requireNonNull(e.getCurrentItem()).hasItemMeta() || !(hasItemId(e.getCurrentItem())))
                return;

            new ForgeConfirmationGui(player, ForgeRecipes.valueOf(getItemId(e.getCurrentItem())).getBaseForgeRecipe()).openInventory(player);
        }

        if(selectedSlot >= ForgeCategoriesGui.MATERIAL_CATEGORY_SLOT && selectedSlot <= ForgeCategoriesGui.STORAGE_CATEGORY_SLOT){
            int categoryOrdinal = selectedSlot - 38; // Find a better way?
            new ForgeCategoriesGui(player, ForgeCategories.values()[categoryOrdinal]).open();
        }

        if(selectedSlot == ForgeCategoriesGui.BACK_SLOT){
            new ForgeGui(player).open();
        }

        if(selectedSlot == ForgeCategoriesGui.EXIT_SLOT){
            player.closeInventory();
        }
    }
}
