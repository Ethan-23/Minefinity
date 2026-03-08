package org.evasive.me.minefinity.smelter.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseFuelItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.framework.CustomItemStack;
import org.evasive.me.minefinity.smelter.service.SmelterHandler;
import org.evasive.me.minefinity.smelter.service.SmelterService;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.*;
import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.utils.guis.SwapItems.swapCursor;

public class SmelterGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int STATUS_SLOT = 23;
    private static final int RECIPE_SLOT = 14;
    private static final int FUEL_SLOT = 32;
    private static final int OUTPUT_SLOT = 25;
    private static final int INFORMATION_SLOT = 49;
    private static final List<Integer> INPUT_SLOTS = List.of(10, 11, 12, 19, 20, 21, 28, 29, 30);
    private final SmelterService smelterService;
    private final SmelterHandler smelterHandler;
    private BukkitTask refreshTask;

    public SmelterGui(Player player) {
        super(player, 54, TextConversions.parse("Smeltery"));
        this.smelterService = Minefinity.getCore().getSmelterService();
        this.smelterHandler = new SmelterHandler();
        build();
    }

    @Override
    protected void build() {
        buildBorder();
        buildFuel();
        buildOutput();
        buildStatus();
        buildInformation();
        buildRecipeSlot();
        buildInputSlots();
        startRefreshTask();
    }

    private void buildInputSlots() {
        for(int i = 0; i < INPUT_SLOTS.size(); i++){
            CustomItemStack customItemStack = smelterService.getInventory(player)[i];
            if(customItemStack == null){
                inventory.setItem(INPUT_SLOTS.get(i), new ItemStack(Material.AIR));
                continue;
            }


            ItemStack itemStack = customItemStack.getCustomItem().getBaseItem().buildItem();
            itemStack.setAmount(customItemStack.getAmount());

            inventory.setItem(INPUT_SLOTS.get(i), itemStack);
        }
    }

    private void buildRecipeSlot(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, TextConversions.parse("<blue>Recipes"));
        itemBuilder.addLore("<gray>Left-Click to view recipes");
        inventory.setItem(RECIPE_SLOT, itemBuilder.build());
    }

    private void buildStatus() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.BROWN_DYE, TextConversions.parse("<yellow>Idle"));
        CustomItem currentSmelt = CustomItemRegistry.getByID(smelterService.getCurrentlySmelting(player));

        if(currentSmelt != null) {
            itemBuilder.setDisplayName("<yellow>Smelting").setItemModel(Material.CAMPFIRE.getKey());
            itemBuilder.addLore("<gray>Item: <green>" + currentSmelt.getID());
            //itemBuilder.addLore("<gray>Time Left: <gold>" + (SmelterRecipes.valueOf(currentSmelt.getID()).getSmelterRecipe().getFuelCost() - smelterService.getCurrentSmeltProgress(player)) + "s");
        }

        if(smelterService.getTotalFuel(player) <= 0 && smelterService.getFuelEfficiency(player) <= 0){
            itemBuilder.addLore("<bold><red>Out of fuel");
        }else{
            itemBuilder.addLore("<gray>Current Fuel: " + smelterService.getFuelEfficiency(player));
        }

        inventory.setItem(STATUS_SLOT, itemBuilder.build());
    }

    private void buildFuel() {

        String fuelId = smelterService.getFuelId(player);
        int totalFuel = smelterService.getTotalFuel(player);

        BaseFuelItem baseFuelItem = (BaseFuelItem) CustomItemRegistry.getByID(fuelId);

        if(baseFuelItem == null || totalFuel == 0){
            ItemBuilder itemBuilder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE, TextConversions.parse("<bold><gold>Fuel Input"));
            inventory.setItem(FUEL_SLOT, itemBuilder.build());
        } else {

            ItemStack fuelItem = baseFuelItem.buildItem();
            fuelItem.setAmount(smelterService.getTotalFuel(player));
            inventory.setItem(FUEL_SLOT, fuelItem);
        }


    }

    private void buildOutput() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.FURNACE_MINECART, TextConversions.parse("<bold><gold>Output"));
        itemBuilder.addLore("<gray>Items:");
        for (Map.Entry<String, Integer> entry : smelterService.getOutput(player).entrySet()) {
            if(entry.getValue() == 0) continue;
            itemBuilder.addLore(CustomItemRegistry.getByID(entry.getKey()).getID() + " x" + entry.getValue());
        }
        inventory.setItem(OUTPUT_SLOT, itemBuilder.build());
    }

    private void buildInformation() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.KNOWLEDGE_BOOK, TextConversions.parse("<bold><yellow>Smelter"));
        itemBuilder.addLore("<gray>Input items you want to smelt on the left side. The smelter will automatically smelt items after their smelt time if there is enough fuel.");
        inventory.setItem(INFORMATION_SLOT, itemBuilder.build());
    }

    private void buildBorder(){
        for(int i = 0; i < INVENTORY_SIZE; i++){
            if(INPUT_SLOTS.contains(i) || List.of(STATUS_SLOT, OUTPUT_SLOT, INFORMATION_SLOT, FUEL_SLOT, RECIPE_SLOT).contains(i)) continue;
            inventory.setItem(i, fillerPane);
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);

        int slot = e.getSlot();
        e.setCancelled(true);

        if(slot == RECIPE_SLOT){
            //need to add a /recipe to see where to get each item idk how complicated but think it will work
        }

        ItemStack cursorItem = e.getCursor();

        if(slot == FUEL_SLOT){
            updateFuelCount(e);
            return;
        }

        if(INPUT_SLOTS.contains(slot)){
            if(getRegisteredBaseItem(cursorItem) == null)
                return;
            updateInventorySlot(e, slot);
        }

        if(slot == OUTPUT_SLOT){
            smelterHandler.handleCollectSlot(e, player);
        }
    }

    private void updateFuelCount(InventoryClickEvent e){

        ItemStack cursorItem = e.getCursor();

        BaseFuelItem replacementFuelItem = (BaseFuelItem) getRegisteredBaseItem(cursorItem);
        BaseFuelItem currentFuelItem = (BaseFuelItem) CustomItemRegistry.getByID(smelterService.getFuelId(player));

        if(replacementFuelItem == null && currentFuelItem == null) return;

        if(replacementFuelItem == null || currentFuelItem == null || !Objects.equals(smelterService.getFuelId(player), replacementFuelItem.getID())) {
            if(currentFuelItem == null)
                swapCursor(e, null, 0);
            else
                swapCursor(e, currentFuelItem, smelterService.getTotalFuel(player));

            smelterService.setFuel(player, replacementFuelItem.getID(), cursorItem.getAmount());
        } else if(Objects.equals(smelterService.getFuelId(player), replacementFuelItem.getID())){
            int currentAmount = smelterService.getTotalFuel(player);
            int addingAmount = cursorItem.getAmount();
            cursorItem.setAmount(Math.max(currentAmount + addingAmount - 64, 0));
            smelterService.setFuel(player, replacementFuelItem.getID(), Math.min(currentAmount + addingAmount, 64));
        }
        rebuildInventory();
    }

    private void updateInventorySlot(InventoryClickEvent e, int slot){
        int inventorySlot = INPUT_SLOTS.indexOf(slot);
        ItemStack cursorItem = e.getCursor();
        CustomItemStack currentCustomItemStack = smelterService.getInventoryItem(player, inventorySlot);
        CustomItemStack replacementCustomItemStack = cursorItem.isEmpty() ? null : new CustomItemStack((BaseCustomItem) CustomItemRegistry.getByID(getItemId(cursorItem)), cursorItem.getAmount());

        if(currentCustomItemStack == null && replacementCustomItemStack == null) return;

        if(currentCustomItemStack == null || replacementCustomItemStack == null || currentCustomItemStack.getCustomItem() != replacementCustomItemStack.getCustomItem()){
            if(currentCustomItemStack == null)
                swapCursor(e, null, 0);
            else
                swapCursor(e, currentCustomItemStack.getCustomItem(), currentCustomItemStack.getAmount());
            smelterService.setInventoryItem(player, replacementCustomItemStack, inventorySlot);
        }else if(currentCustomItemStack.getCustomItem() == replacementCustomItemStack.getCustomItem()){
            int currentAmount = currentCustomItemStack.getAmount();
            int addingAmount = replacementCustomItemStack.getAmount();
            replacementCustomItemStack.setAmount(Math.min(currentAmount + addingAmount, 64));
            cursorItem.setAmount(Math.max(currentAmount + addingAmount - 64, 0));
            smelterService.setInventoryItem(player, replacementCustomItemStack, inventorySlot);
        }

        rebuildInventory();
    }

    private void refreshDynamicSlots() {
        buildStatus();
        buildFuel();
        buildOutput();
        buildInputSlots();
    }

    private boolean isStillOpen() {
        return player.getOpenInventory().getTopInventory().getHolder() instanceof SmelterGui;
    }

    private void startRefreshTask() {
        refreshTask = Bukkit.getScheduler().runTaskTimer(
                Minefinity.getCore(),
                () -> {

                    if (!isStillOpen()) {
                        refreshTask.cancel();
                        return;
                    }

                    refreshDynamicSlots();

                },
                20L,
                20L
        );
    }


    @Override
    public void rebuildInventory() {
        Bukkit.getScheduler().runTask(Minefinity.getCore(), this::refreshDynamicSlots);
    }
}
