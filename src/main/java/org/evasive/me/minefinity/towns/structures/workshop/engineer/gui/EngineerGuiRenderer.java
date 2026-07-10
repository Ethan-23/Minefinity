package org.evasive.me.minefinity.towns.structures.workshop.engineer.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.WorkshopMode;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.BaseWorkshopRecipe;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.WorkshopRecipeManager;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.service.EngineerService;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.tools.WorkshopToolsTiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.core.utils.TextConversions.buildRarityColor;
import static org.evasive.me.minefinity.core.utils.TextConversions.formatItemName;
import static org.evasive.me.minefinity.towns.structures.workshop.engineer.gui.EngineerGui.*;

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
    private final WorkshopRecipeManager workshopRecipeManager;
    private final CustomItemRegistryService customItemRegistryService;

    public EngineerGuiRenderer(Player player, WorkshopMode mode, EngineerService service, WorkshopRecipeManager workshopRecipeManager, CustomItemRegistryService customItemRegistryService) {
        this.player = player;
        this.mode = mode;
        this.service = service;
        this.workshopRecipeManager = workshopRecipeManager;
        this.customItemRegistryService = customItemRegistryService;
    }

    public void render(Inventory inventory) {
        GuiUtils.fillGui(inventory);
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
        CustomItemBuilder itemBuilder = new CustomItemBuilder(Material.KNOWLEDGE_BOOK, INFORMATION_TITLE);
        itemBuilder.addLore(INFORMATION_LORE);
        inventory.setItem(INFORMATION_SLOT, itemBuilder.build());
    }

    private void renderHeader(Inventory inventory) {

        boolean isCarpentry = mode == WorkshopMode.CARPENTRY;

        CustomItemBuilder skillHeader = new CustomItemBuilder(isCarpentry ? Material.COPPER_AXE : Material.COPPER_SPEAR, "<bold><gold>" + formatItemName(mode.name()));
        skillHeader.addLore(isCarpentry ? WOODWORKING_HEADER_LORE : STONEWORKING_HEADER_LORE);
        inventory.setItem(HEADER_SLOT,  skillHeader.build());
    }

    private void renderResourceSlot(Inventory inventory) {

        WorkshopToolsTiers storedResource = service.getWorkshopCurrentResource(player, mode);

        if(storedResource == null){
            inventory.setItem(RESOURCE_SLOT, buildEmptyResource());
            return;
        }

        CustomItemBuilder itemBuilder = new CustomItemBuilder(customItemRegistryService.getRegisteredItemStack(storedResource.name()));
        itemBuilder.setAmount(service.getWorkshopCurrentResourceCount(player, mode));
        inventory.setItem(RESOURCE_SLOT, itemBuilder.build());
    }

    private ItemStack buildEmptyResource(){
        CustomItemBuilder emptyResourceSlot = new CustomItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, EMPTY_RESOURCE_TITLE);
        emptyResourceSlot.addLore(EMPTY_RESOURCE_LORE);
        for(int i = 0; WorkshopToolsTiers.values().length > i; i++) {
            emptyResourceSlot.addLore(buildRarityColor(WorkshopToolsTiers.values()[i].name(), Rarity.values()[i]) + " <gray>x4");
        }
        return emptyResourceSlot.build();
    }

    private void renderToolSlot(Inventory inventory) {

        WorkshopToolsTiers toolType = service.getWorkshopToolType(player, mode);

        if(toolType == null){
            CustomItemBuilder emptyTool = new CustomItemBuilder(Material.IRON_BARS, EMPTY_TOOL_TITLE);
            emptyTool.addLore(EMPTY_TOOL_LORE);
            inventory.setItem(TOOL_SLOT, emptyTool.build());
            return;
        }

        int durability = service.getWorkshopToolDurability(player, mode);
        CustomItemBuilder tool = new CustomItemBuilder(customItemRegistryService.getRegisteredItemStack(toolType.name()));
        tool.setLore(new ArrayList<>());
        tool.addLore("<gray>Durability: <yellow>" + durability);
        tool.addBlank().addLore("<gray>Shift Right-Click to trash tool.");
        tool.setAmount(Math.min(64, durability));

        int toolTier = service.getWorkshopToolType(player, mode).ordinal();
        tool.setItemModel(mode == WorkshopMode.CARPENTRY ? carpentryToolMaterials.get(toolTier).getKey() :  stoneworkingToolMaterials.get(toolTier).getKey());
        tool.setDisplayName("<bold>"+ TextConversions.buildRarityColor(service.getWorkshopToolType(player, mode).name().replace("_INGOT", ""), customItemRegistryService.getBaseItemById(service.getWorkshopToolType(player, mode).name()).getRarity()) + (mode == WorkshopMode.CARPENTRY ? " Axe" : " Chisel"));

        inventory.setItem(TOOL_SLOT, tool.build());
    }

    private void renderSwapButton(Inventory inventory){
        boolean isCarpentry = mode == WorkshopMode.CARPENTRY;
        CustomItemBuilder itemBuilder = new CustomItemBuilder(isCarpentry ? Material.GRINDSTONE : Material.STONECUTTER, "<yellow>Swap to <bold><gold>"+formatItemName(isCarpentry ? WorkshopMode.STONEWORKING.name() : WorkshopMode.CARPENTRY.name()));
        inventory.setItem(SWAP_SLOT, itemBuilder.build());
    }

    private void renderShopOptions(Inventory inventory){

        int resourceCount = 0;

        for (BaseWorkshopRecipe workshopRecipe : workshopRecipeManager.getRecipes().values()) {

            if(workshopRecipe.getRequiredToolType() != mode)
                continue;

            CustomItemBuilder shopItem = new CustomItemBuilder(customItemRegistryService.getRegisteredItemStack(workshopRecipe.getResult()));
            shopItem.setLore(new ArrayList<>());
            shopItem.addLore("<gray>Required Tool: " + workshopRecipe.getRequiredToolsTier().name());
            shopItem.addLore("<gray>Durability Usage: <yellow>" + workshopRecipe.getDurabilityUsage());
            shopItem.addLore("<gray>Recipe:");

            for(Map.Entry<String, Integer> entry : workshopRecipe.getRecipe().entrySet()){
                String itemId = entry.getKey();
                int amount = entry.getValue();

                BaseCustomItem customItem = customItemRegistryService.getRegisteredBaseItem(itemId);

                if(customItem != null){
                    shopItem.addLore("<gray>- " + customItem.getDisplayName() + " <gray>x" + amount);
                }else {
                    shopItem.addLore("<gray>- Unknown Item <bold><red>" +  itemId);
                }


            }

            inventory.setItem(SHOP_SLOTS.get(resourceCount), shopItem.build());
            resourceCount++;
        }
    }

}
