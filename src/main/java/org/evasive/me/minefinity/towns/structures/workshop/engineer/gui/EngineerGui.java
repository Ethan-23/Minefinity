package org.evasive.me.minefinity.towns.structures.workshop.engineer.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.WorkshopMode;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.WorkshopRecipeManager;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.service.EngineerClickHandler;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.service.EngineerService;

import java.util.List;

import static org.evasive.me.minefinity.core.utils.TextConversions.formatItemName;

public class EngineerGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    public static final int HEADER_SLOT = 13;
    public static final int TOOL_SLOT = 40;
    public static final int RESOURCE_SLOT = 41;
    public static final int INFORMATION_SLOT = 49;
    public static final int SWAP_SLOT = 39;
    public static final List<Integer> SHOP_SLOTS = List.of(
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    );

    private final EngineerGuiRenderer renderer;
    EngineerClickHandler engineerClickHandler;

    public EngineerGui(Player player, WorkshopMode workshopMode, EngineerService engineerService, WorkshopRecipeManager workshopRecipeManager, BackpackService backpackService, CustomItemRegistryService customItemRegistryService, RecipeService recipeService) {
        super(player, INVENTORY_SIZE, TextConversions.parse(formatItemName(workshopMode.name())));
        renderer = new EngineerGuiRenderer(player, workshopMode, engineerService, workshopRecipeManager, customItemRegistryService);
        engineerClickHandler = new EngineerClickHandler(player, workshopMode, engineerService, workshopRecipeManager, backpackService, customItemRegistryService, recipeService);
        build();
    }

    @Override
    protected void build() {
        renderer.render(inventory, INVENTORY_SIZE);
    }

    public void rebuildDynamic(){
        renderer.renderDynamic(inventory);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);
        engineerClickHandler.handleEngineerClick(e);
        rebuildDynamic();
    }
}