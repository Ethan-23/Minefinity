package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.ConfirmationGui;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.*;
import org.evasive.me.minefinity.customItems.itembuilder.handler.ItemCreationHandler;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.itembuilder.registry.config.RegistryConfigHandler;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.List;
import java.util.Optional;

import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;

public class ItemCreationGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int BUILT_ITEM_SLOT = 13;
    private static final int REGISTRY_SAVE_SLOT = 22;
    private static final List<Integer> OPTION_SLOTS = List.of(28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43, 46, 47, 48, 49, 50, 51, 52);
    BaseCustomItem baseCustomItem;
    CustomItemType customItemType;
    ItemCreationHandler itemCreationHandler;
    RegistryConfigHandler registryConfigHandler;

    public ItemCreationGui(Player player, BaseCustomItem baseCustomItem, RegistryConfigHandler registryConfigHandler) {
        super(player, INVENTORY_SIZE, TextConversions.parse("ItemBuilder"));
        this.customItemType = baseCustomItem.getCustomItemType();
        this.baseCustomItem = baseCustomItem;
        this.registryConfigHandler = registryConfigHandler;
        itemCreationHandler = new ItemCreationHandler();
        build();
        updateItem();
    }

    @Override
    protected void build() {
        fillInventory();
        loadCustomItemOptions();
        addDisplayItem();
        addRegistryButton();
    }

    private void fillInventory(){
        for(int i = 0; i < INVENTORY_SIZE; i++){
            inventory.setItem(i, fillerPane);
        }
    }

    private void addDisplayItem(){
        inventory.setItem(BUILT_ITEM_SLOT, baseCustomItem.buildItem());
    }

    public void addRegistryButton(){
        boolean registered = CustomItemRegistry.isRegistered(baseCustomItem.getID());
        Material pane = registered ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE;
        String save = registered ? "<bold><yellow>Override" : "<bold><green>Save";
        String text = registered ? "<yellow>Left Click <gray>to <bold><yellow>OVERRIDE <reset><gray>in the item registry. This will update all items with this id globally." : "<green>Left Click <gray>to <bold><green>save <reset><gray>to the item registry.";
        ItemBuilder saveButton = new ItemBuilder(pane, TextConversions.parse(save));
        saveButton.addBlank().addLore(text);
        inventory.setItem(REGISTRY_SAVE_SLOT, saveButton.build());
    }

    public void updateItem(){
        PlayerInventory playerInventory = player.getInventory();
        ItemStack playerItem = playerInventory.getItemInMainHand();
        ItemStack itemStack = baseCustomItem.buildItem();
        if(playerItem.isEmpty()){
            playerInventory.setItemInMainHand(itemStack);
            return;
        }
        int amount = player.getInventory().getItemInMainHand().getAmount();
        itemStack.setAmount(amount);
        player.getInventory().setItemInMainHand(itemStack);
    }

    private void loadCustomItemOptions(){
        for(int i = 0; i < OPTION_SLOTS.size(); i++){
            List<ItemOptions> customItemOptions = customItemType.getAllOptions();
            if(customItemOptions.size() > i){
                ItemOptions itemOption = customItemOptions.get(i);
                ItemBuilder optionItemStack = new ItemBuilder(itemOption.getMaterial(), "<bold><gold>" + TextConversions.formatItemName(itemOption.name()));
                String[] itemTypeString = itemOption.getClassType().getName().split("\\.");
                optionItemStack.addLore("<yellow>Current Value: <gray>" + itemOption.get(baseCustomItem));
                optionItemStack.addLore("<yellow>Data Type: <gray>" + itemTypeString[itemTypeString.length - 1]);
                if(List.of(ItemOptions.MATERIAL, ItemOptions.VISUAL_MATERIAL, ItemOptions.PICKAXE_HEAD, ItemOptions.PICKAXE_HANDLE, ItemOptions.PICKAXE_CORE, ItemOptions.STORAGE_LIST).contains(itemOption))
                    optionItemStack.addLore("<gray>You can drop items into this!");
                if(List.of(ItemOptions.COMPONENT_ABILITY, ItemOptions.STORAGE_LIST).contains(itemOption))
                    optionItemStack.addLore("<yellow>Shift Left-Click <gray>to change order");
                inventory.setItem(OPTION_SLOTS.get(i), optionItemStack.build());
            }
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        super.onClick(e);
        e.setCancelled(true);

        int slot = e.getSlot();

        if(e.getClick().isLeftClick() && slot == REGISTRY_SAVE_SLOT){
            new ConfirmationGui(player, this, p -> {
                player.sendMessage(TextConversions.parse("<green>Item has been saved to the registry!"));
                registryConfigHandler.addSingleEntry(baseCustomItem);
                CustomItemRegistry.overrideCustomItem(baseCustomItem);
                //reopen();
            }).open();
            return;
        }

        int optionNumber = OPTION_SLOTS.indexOf(slot);
        if (optionNumber == -1) return;

        List<ItemOptions> optionList = customItemType.getAllOptions();

        if(optionList.size() <= optionNumber)
            return;

        ItemOptions clickedOption = optionList.get(optionNumber);

        if(e.getClick() == ClickType.SHIFT_LEFT){
            if(clickedOption == ItemOptions.COMPONENT_ABILITY)
                new ListOrderGui(player, ((BasePickaxeComponent)baseCustomItem).getPickaxeAbilityList(), this).open();
            else if (clickedOption == ItemOptions.STORAGE_LIST) {
                new ListOrderGui(player, ((BaseBackpackItem)baseCustomItem).getStoredItemIdList(), this).open();
            }
            return;
        }

        if(clickedOption.get(baseCustomItem) instanceof Optional<?> && e.getClick().isRightClick()){
            clickedOption.apply(baseCustomItem, null);
            reopen();
            return;
        }

        ItemStack cursorItem = e.getCursor();

        if (clickedOption.isEnum()) {

            @SuppressWarnings("unchecked")
            Class<? extends Enum<?>> rawClass = (Class<? extends Enum<?>>) clickedOption.getClassType();
            openEnumOptionGui((Class) rawClass, clickedOption);

        } else if (clickedOption.isString()) {
            itemCreationHandler.handleStringChanges(player, clickedOption, baseCustomItem, cursorItem, this);
        } else if (clickedOption.isBoolean()) {
            itemCreationHandler.handleBooleanChanges(clickedOption, baseCustomItem, this);
        }else if (clickedOption.isFloat()) {
            itemCreationHandler.handleFloatChanges(player, clickedOption, baseCustomItem, this);
        } else if (clickedOption.isInteger()) {
            itemCreationHandler.handleInteger(player, clickedOption, baseCustomItem, this);
        }

    }

    private <E extends Enum<E>> void openEnumOptionGui(Class<E> enumClass, ItemOptions option) {
        new OptionsGui<>(player, enumClass, selected -> {
            option.apply(baseCustomItem, selected);
            if(enumClass == CustomItemType.class){
                this.customItemType = (CustomItemType) selected;
                buildNewItemType();
            }
            reopen();
        }, baseCustomItem).open();
    }

    private void buildNewItemType(){
        baseCustomItem = customItemType.create(baseCustomItem.buildItem());
    }

    public void reopen(){
        updateItem();
        build();
        open();
    }
}
