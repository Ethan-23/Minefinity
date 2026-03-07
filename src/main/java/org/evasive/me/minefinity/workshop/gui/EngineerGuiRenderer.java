package org.evasive.me.minefinity.workshop.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;
import org.evasive.me.minefinity.workshop.WorkshopMode;
import org.evasive.me.minefinity.workshop.recipes.BaseWorkshopRecipe;
import org.evasive.me.minefinity.workshop.recipes.WorkshopRecipes;
import org.evasive.me.minefinity.workshop.service.EngineerService;
import org.evasive.me.minefinity.workshop.tools.WorkshopToolsTiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.utils.TextConversions.buildRarityColor;
import static org.evasive.me.minefinity.utils.TextConversions.formatItemName;
import static org.evasive.me.minefinity.workshop.gui.EngineerGui.*;

public class EngineerGuiRenderer {

    //Header Materials
    private static final List<Material> carpentryToolMaterials = List.of(Material.STONE_AXE, Material.COPPER_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE);
    private static final List<Material> stoneworkingToolMaterials = List.of(Material.STONE_SPEAR, Material.COPPER_SPEAR, Material.IRON_SPEAR, Material.GOLDEN_SPEAR, Material.DIAMOND_SPEAR);

    //Header Text
    private static final String STONEWORKING_HEADER_LORE = "<gray>Where stone takes form";
    private static final String WOODWORKING_HEADER_LORE = "<gray>From raw wood to refined craft";

    //Tool Text
    private static final String EMPTY_TOOL_TITLE = "<red>No Tool";
    private static final String EMPTY_TOOL_LORE = "<gray>Put items in the resource slot to create a tool.";

    //Resource Text
    private static final String EMPTY_RESOURCE_TITLE = "<yellow>Tool Components";
    private static final String EMPTY_RESOURCE_LORE = "<gray>Drag & Drop resources to create a tool.";

    //Information Book
    private static final String INFORMATION_TITLE = "<bold><yellow>Engineer";
    private static final List<String> INFORMATION_LORE = List.of(
            "<gray>The Engineer specializes in Carpentry and Stoneworking. Use the mode button to switch between crafting types. Each mode has its own set of recipes.",
            "",
            "<gray>To craft, the Engineer requires tools. Insert 4 of a resource into the resource slot to create its corresponding tool. For example, Flint creates a Flint Tool.",
            "",
            "<gray>Higher tier resources create stronger tools. Higher tier tools unlock better recipes and have greater durability.",
            "",
            "<gray>Each craft consumes tool durability. When durability reaches zero, the tool breaks and must be recreated by providing the required resources again."
    );

    private final Player player;
    private final WorkshopMode mode;
    private final EngineerService service;

    public EngineerGuiRenderer(Player player, WorkshopMode mode, EngineerService service) {
        this.player = player;
        this.mode = mode;
        this.service = service;
    }

    public void render(Inventory inventory, int inventorySize) {
        renderBorder(inventory, inventorySize);
        renderHeader(inventory);
        renderSwapButton(inventory);
        renderShopOptions(inventory);
        renderResourceSlot(inventory);
        renderToolSlot(inventory);
        renderInfo(inventory);
    }

    public void renderDynamic(Inventory inventory) {
        renderResourceSlot(inventory);
        renderToolSlot(inventory);
    }

    private void renderInfo(Inventory inventory){
        ItemBuilder itemBuilder = new ItemBuilder(Material.KNOWLEDGE_BOOK, INFORMATION_TITLE);
        itemBuilder.addLore(INFORMATION_LORE);
        inventory.setItem(INFORMATION_SLOT, itemBuilder.build());
    }

    private void renderHeader(Inventory inventory) {

        boolean isCarpentry = mode == WorkshopMode.CARPENTRY;

        ItemBuilder skillHeader = new ItemBuilder(isCarpentry ? Material.COPPER_AXE : Material.COPPER_SPEAR, "<bold><gold>" + formatItemName(mode.name()));
        skillHeader.addLore(isCarpentry ? WOODWORKING_HEADER_LORE : STONEWORKING_HEADER_LORE);
        inventory.setItem(HEADER_SLOT,  skillHeader.build());
    }

    private void renderResourceSlot(Inventory inventory) {

        WorkshopToolsTiers storedResource = service.getWorkshopCurrentResource(player, mode);

        if(storedResource == null){
            inventory.setItem(RESOURCE_SLOT, buildEmptyResource());
            return;
        }

        ItemBuilder itemBuilder = new ItemBuilder(CustomItemRegistry.getByID(storedResource.name()).getBaseItem().buildItem());
        itemBuilder.setAmount(service.getWorkshopCurrentResourceCount(player, mode));
        inventory.setItem(RESOURCE_SLOT, itemBuilder.build());
    }

    private ItemStack buildEmptyResource(){
        ItemBuilder emptyResourceSlot = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, EMPTY_RESOURCE_TITLE);
        emptyResourceSlot.addLore(EMPTY_RESOURCE_LORE);
        for(int i = 0; WorkshopToolsTiers.values().length > i; i++) {
            emptyResourceSlot.addLore(buildRarityColor(WorkshopToolsTiers.values()[i].name(), Rarity.values()[i]) + " <gray>x4");
        }
        return emptyResourceSlot.build();
    }

    private void renderToolSlot(Inventory inventory) {

        WorkshopToolsTiers toolType = service.getWorkshopToolType(player, mode);

        if(toolType == null){
            ItemBuilder emptyTool = new ItemBuilder(Material.IRON_BARS, EMPTY_TOOL_TITLE);
            emptyTool.addLore(EMPTY_TOOL_LORE);
            inventory.setItem(TOOL_SLOT, emptyTool.build());
            return;
        }

        int durability = service.getWorkshopToolDurability(player, mode);
        ItemBuilder tool = new ItemBuilder(CustomItemRegistry.getByID(toolType.name()).getBaseItem().buildItem());
        tool.setLore(new ArrayList<>());
        tool.addLore("<gray>Durability: <yellow>" + durability);
        tool.addBlank().addLore("<gray>Shift Right-Click to trash tool.");
        tool.setAmount(Math.min(64, durability));

        int toolTier = service.getWorkshopToolType(player, mode).ordinal();
        tool.setItemModel(mode == WorkshopMode.CARPENTRY ? carpentryToolMaterials.get(toolTier).getKey() :  stoneworkingToolMaterials.get(toolTier).getKey());
        tool.setDisplayName("<bold>"+ TextConversions.buildRarityColor(service.getWorkshopToolType(player, mode).name().replace("_INGOT", ""), CustomItemRegistry.getByID(service.getWorkshopToolType(player, mode).name()).getBaseItem().getRarity()) + (mode == WorkshopMode.CARPENTRY ? " Axe" : " Chisel"));

        inventory.setItem(TOOL_SLOT, tool.build());
    }

    private void renderBorder(Inventory inventory, int size){
        for(int i = 0; i < size; i++){
            if(SHOP_SLOTS.contains(i) || List.of(SWAP_SLOT, HEADER_SLOT, TOOL_SLOT, RESOURCE_SLOT, INFORMATION_SLOT).contains(i))
                continue;

            inventory.setItem(i, fillerPane);
        }
    }

    private void renderSwapButton(Inventory inventory){
        boolean isCarpentry = mode == WorkshopMode.CARPENTRY;
        ItemBuilder itemBuilder = new ItemBuilder(isCarpentry ? Material.GRINDSTONE : Material.STONECUTTER, "<yellow>Swap to <bold><gold>"+formatItemName(isCarpentry ? WorkshopMode.STONEWORKING.name() : WorkshopMode.CARPENTRY.name()));
        inventory.setItem(SWAP_SLOT, itemBuilder.build());
    }

    private void renderShopOptions(Inventory inventory){

        int resourceCount = 0;
        for(WorkshopRecipes recipe : WorkshopRecipes.values()){
            BaseWorkshopRecipe workshopRecipe = recipe.getRecipe();

            ItemBuilder shopItem = new ItemBuilder(workshopRecipe.getResult().getBaseItem().buildItem());
            shopItem.setLore(new ArrayList<>());
            shopItem.addLore("<gray>Required Tool: " + buildRarityColor(workshopRecipe.getRequiredToolsTier().name(), CustomItemRegistry.getByID(workshopRecipe.getRequiredToolsTier().name()).getBaseItem().getRarity()));
            shopItem.addLore("<gray>Durability Usage: <yellow>" + workshopRecipe.getDurabilityUsage());
            shopItem.addLore("<gray>Recipe:");

            for(Map.Entry<CustomItem, Integer> entry : workshopRecipe.getRecipe().entrySet()){
                String itemName = entry.getKey().getID();
                int amount = entry.getValue();
                CustomItem customItem = entry.getKey();
                shopItem.addLore("<gray>- " + buildRarityColor(itemName, ((BaseCustomItem)customItem.getBaseItem()).getRarity()) + " <gray>x" + amount);
            }

            if(workshopRecipe.getRequiredToolType() != mode)
                continue;

            inventory.setItem(SHOP_SLOTS.get(resourceCount), shopItem.build());
            resourceCount++;
        }
    }

}
