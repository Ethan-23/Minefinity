package org.evasive.me.minefinity.workshop;

import org.apache.logging.log4j.message.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.items.ResourceItem;
import org.evasive.me.minefinity.player.sevices.EngineerService;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.ItemFunctions.hasItemId;
import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.utils.TextConversions.buildRarityColor;
import static org.evasive.me.minefinity.utils.TextConversions.formatItemName;

public class EngineerGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int HEADER_SLOT = 13;
    private static final int TOOL_SLOT = 40;
    private static final int RESOURCE_SLOT = 41;
    private static final int INFORMATION_SLOT = 49;
    private static final int SWAP_SLOT = 39;
    private static final List<Integer> SHOP_SLOTS = List.of(19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
    private final WorkshopMode workshopMode;
    private final EngineerHandler engineerHandler;
    private final EngineerService engineerService;
    private static final List<Material> carpentryToolMaterials = List.of(Material.STONE_AXE, Material.COPPER_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE);
    private static final List<Material> stoneworkingToolMaterials = List.of(Material.STONE_SPEAR, Material.COPPER_SPEAR, Material.IRON_SPEAR, Material.GOLDEN_SPEAR, Material.DIAMOND_SPEAR);


    public EngineerGui(Player player, WorkshopMode workshopMode) {
        super(player, INVENTORY_SIZE, TextConversions.parse(formatItemName(workshopMode.name())));
        this.workshopMode = workshopMode;
        this.engineerService = Minefinity.getCore().getEngineerService();
        this.engineerHandler = new EngineerHandler();
        build();
    }

    @Override
    protected void build() {
        buildBorder();
        buildButtons();
        buildShopOptions();
        buildResourceSlots();
        buildWorkshopInformation();
        buildToolSlots();
    }

    private void buildWorkshopInformation() {

        ItemStack skillHeader = new ItemBuilder(this.workshopMode == WorkshopMode.CARPENTRY ? Material.COPPER_AXE : Material.COPPER_SPEAR, TextConversions.parse("<bold><gold>" + formatItemName(this.workshopMode.name()))).build();
        inventory.setItem(HEADER_SLOT, skillHeader);

        ItemBuilder itemBuilder = new ItemBuilder(Material.KNOWLEDGE_BOOK, TextConversions.parse("<bold><yellow>Engineer"));
        itemBuilder.addLore("<gray>This is the engineer. He is proficient in Carpentry and Stoneworking. In the bottom right you can swap his skill to change what you are crafting.");
        itemBuilder.addBlank();
        itemBuilder.addLore("<gray>The engineer requires you to give him resources to craft and repair his tools. He requires 4x resources per tool craft. Higher tier tools give you access to higher crafts and last longer.");
        inventory.setItem(INFORMATION_SLOT, itemBuilder.build());
    }

    private void buildResourceSlots() {

        WorkshopToolsTiers storedResource = engineerService.getWorkshopCurrentResource(player, workshopMode);

        if(storedResource == null){
            inventory.setItem(RESOURCE_SLOT, buildEmptyResource());
            return;
        }

        ItemBuilder itemBuilder = new ItemBuilder(ResourceItem.valueOf(storedResource.name()).getBuilder().buildItem());
        itemBuilder.setAmount(engineerService.getWorkshopCurrentResourceCount(player, workshopMode));
        inventory.setItem(RESOURCE_SLOT, itemBuilder.build());
    }

    private ItemStack buildEmptyResource(){
        ItemBuilder emptyResourceSlot = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, TextConversions.parse("Resource Slot"));
        emptyResourceSlot.addLore("<yellow>Requested Resources:");
        for(int i = 0; WorkshopToolsTiers.values().length > i; i++) {
            emptyResourceSlot.addLore(buildRarityColor(WorkshopToolsTiers.values()[i].name(), Rarity.values()[i]) + " <gray>x4");
        }
        return emptyResourceSlot.build();
    }

    private void buildToolSlots() {

        WorkshopToolsTiers toolType = engineerService.getWorkshopToolType(player, workshopMode);

        if(toolType == null){
            ItemBuilder emptyTool = new ItemBuilder(Material.IRON_BARS, TextConversions.parse("<red>No Tool"));
            emptyTool.addLore("<gray>Put items in the resource slot to create a tool.");
            inventory.setItem(TOOL_SLOT, emptyTool.build());
            return;
        }

        int durability = engineerService.getWorkshopToolDurability(player, workshopMode);
        ItemBuilder tool = new ItemBuilder(ResourceItem.valueOf(toolType.name()).getBuilder().buildItem());
        tool.setLore(new ArrayList<>()).addLore("<gray>Durability: <yellow>" + durability);
        tool.addBlank().addLore("<gray>Right-Click to trash tool.");
        tool.setAmount(Math.min(64, durability));

        int toolTier = engineerService.getWorkshopToolType(player, workshopMode).ordinal();
        tool.setItemModel(workshopMode == WorkshopMode.CARPENTRY ? carpentryToolMaterials.get(toolTier).getKey() :  stoneworkingToolMaterials.get(toolTier).getKey());
        tool.setDisplayName(TextConversions.parse("<bold>"+ TextConversions.buildRarityColor(engineerService.getWorkshopToolType(player, workshopMode).name().replace("_INGOT", ""), ResourceItem.valueOf(engineerService.getWorkshopToolType(player, workshopMode).name()).getBuilder().getRarity()) + (workshopMode == WorkshopMode.CARPENTRY ? " Axe" : " Chisel")));

        inventory.setItem(TOOL_SLOT, tool.build());
    }

    private void buildBorder(){
        for(int i = 0; i < INVENTORY_SIZE; i++){
            if(SHOP_SLOTS.contains(i) || List.of(SWAP_SLOT, HEADER_SLOT, TOOL_SLOT, RESOURCE_SLOT, INFORMATION_SLOT).contains(i))continue;
            inventory.setItem(i, fillerPane);
        }
    }

    private void buildButtons(){
        boolean isCarpentry = this.workshopMode == WorkshopMode.CARPENTRY;
        ItemBuilder itemBuilder = new ItemBuilder(isCarpentry ? Material.GRINDSTONE : Material.STONECUTTER, TextConversions.parse("<yellow>Swap to <bold><gold>"+formatItemName(isCarpentry ? WorkshopMode.STONEWORKING.name() : WorkshopMode.CARPENTRY.name())));
        inventory.setItem(SWAP_SLOT, itemBuilder.build());
    }

    private void buildShopOptions(){

        int resourceCount = 0;
        for(WorkshopRecipes recipe : WorkshopRecipes.values()){
            BaseWorkshopRecipe workshopRecipe = recipe.getRecipe();

            ItemBuilder shopItem = new ItemBuilder(workshopRecipe.getResult().getBuilder().buildItem());
            shopItem.setLore(new ArrayList<>());
            shopItem.addLore("<gray>Required Tool: " + buildRarityColor(workshopRecipe.requiredToolsTier.name(), ResourceItem.valueOf(workshopRecipe.requiredToolsTier.name()).getBuilder().getRarity()));
            shopItem.addLore("<gray>Durability Usage: <yellow>" + workshopRecipe.durabilityUsage);
            shopItem.addLore("<gray>Recipe:");

            for(Map.Entry<CustomItem, Integer> entry : workshopRecipe.getRecipe().entrySet()){
                String itemName = entry.getKey().getID();
                int amount = entry.getValue();
                CustomItem customItem = entry.getKey();
                shopItem.addLore("<gray>- " + buildRarityColor(itemName, customItem.getBuilder().getRarity()) + " <gray>x" + amount);
            }

            if(workshopRecipe.requiredToolType != this.workshopMode)
                continue;

            inventory.setItem(SHOP_SLOTS.get(resourceCount), shopItem.build());
            resourceCount++;
        }
    }

    private boolean canCraftNextTool(){
        WorkshopToolsTiers resourceType = engineerService.getWorkshopCurrentResource(player, workshopMode);
        if(resourceType == null) return false;
        int amount = engineerService.getWorkshopCurrentResourceCount(player, workshopMode);
        return amount >= 4;
    }

    private void craftTool(){
        WorkshopToolsTiers resourceType = engineerService.getWorkshopCurrentResource(player, workshopMode);
        engineerService.setWorkshopToolType(player, workshopMode, resourceType);
        engineerService.setWorkshopToolDurability(player, workshopMode, resourceType.durability);
        int total = engineerService.getWorkshopCurrentResourceCount(player, workshopMode);
        engineerService.setWorkshopCurrentResourceCount(player, workshopMode, total-4);

        if(total-4 == 0){
            engineerService.setWorkshopCurrentResource(player, workshopMode, null);
            engineerService.setWorkshopCurrentResourceCount(player, workshopMode, 0);
        }

        rebuildInventory();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);

        int slot = e.getSlot();

        if(slot == SWAP_SLOT){
            new EngineerGui(player, this.workshopMode == WorkshopMode.CARPENTRY ? WorkshopMode.STONEWORKING : WorkshopMode.CARPENTRY).open();
            e.setCancelled(true);
            return;
        }

        if(slot == RESOURCE_SLOT && e.getClick().isLeftClick()){

            if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.YELLOW_STAINED_GLASS_PANE))
                e.getCurrentItem().setAmount(0);
            rebuildInventory();
            if(!engineerHandler.handleResourceSlot(player, e.getCursor(), workshopMode))
                e.setCancelled(true);
            if(engineerService.getWorkshopToolType(player, workshopMode) == null && canCraftNextTool()){
                craftTool();
            }
            return;
        }

        if(SHOP_SLOTS.contains(slot)){
            e.setCancelled(true);

            ItemStack currentItem = e.getCurrentItem();
            if(!hasItemId(currentItem)) return;
            BaseWorkshopRecipe workshopRecipe = WorkshopRecipes.valueOf(getItemId(currentItem)).baseWorkshopRecipe;

            int toolDurability = engineerService.getWorkshopToolDurability(player, workshopMode);

            if(toolDurability <= 0){
                player.sendMessage(TextConversions.parse("<red>You need to craft a tool."));
                return;
            }

            if(engineerService.getWorkshopToolDurability(player, workshopMode) < toolDurability) return;

            boolean purchased = this.engineerHandler.handlePurchase(player, workshopRecipe);

            if(purchased){
                engineerService.setWorkshopToolDurability(player, workshopMode, toolDurability - workshopRecipe.durabilityUsage);

                if(toolDurability - workshopRecipe.durabilityUsage == 0 && canCraftNextTool()){
                    craftTool();
                }else if (toolDurability - workshopRecipe.durabilityUsage <= 0){
                    engineerService.setWorkshopToolType(player, workshopMode, null);
                }
                rebuildInventory();
            }
            return;
        }

        if(slot == TOOL_SLOT && e.getClick() == ClickType.RIGHT){
            this.engineerService.setWorkshopToolDurability(player, workshopMode, 0);
            this.engineerService.setWorkshopToolType(player, workshopMode, null);
            if(canCraftNextTool()){
                craftTool();
            }
            rebuildInventory();
        }

        e.setCancelled(true);

    }
}
