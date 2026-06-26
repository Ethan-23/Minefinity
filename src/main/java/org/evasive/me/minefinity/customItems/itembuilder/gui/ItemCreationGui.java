package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.ConfirmationGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseBackpackItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.EditableComponent;
import org.evasive.me.minefinity.customItems.itembuilder.events.PlayerInputListener;
import org.evasive.me.minefinity.customItems.itembuilder.handler.ItemCreationHandler;
import org.evasive.me.minefinity.customItems.registry.config.RegistryConfigHandler;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class ItemCreationGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int BUILT_ITEM_SLOT = 13;
    private static final int REGISTRY_SAVE_SLOT = 22;

    private static final List<Integer> OPTION_SLOTS = List.of(
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
            46, 47, 48, 49, 50, 51, 52
    );

    private BaseCustomItem baseCustomItem;
    private CustomItemType customItemType;

    private final CustomItemRegistryService registry;
    private final PlayerInputListener inputListener;

    public ItemCreationGui(Player player,
                           BaseCustomItem item,
                           RegistryConfigHandler registryConfigHandler,
                           PlayerInputListener inputListener,
                           CustomItemRegistryService registry) {
        super(player, INVENTORY_SIZE, TextConversions.parse("ItemBuilder"));

        this.baseCustomItem = item;
        this.customItemType = item.getCustomItemType();

        this.registry = registry;
        this.inputListener = inputListener;

        build();
        updateItem();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);

        loadCustomItemOptions();
        addDisplayItem();
        addRegistryButton();
    }

    private void addDisplayItem() {
        inventory.setItem(
                BUILT_ITEM_SLOT,
                registry.buildItem(baseCustomItem)
        );
    }

    public void addRegistryButton(){
        boolean registered = registry.isRegistered(baseCustomItem.getID());
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
        ItemStack itemStack = registry.buildItem(baseCustomItem);
        if(playerItem.isEmpty()){
            playerInventory.setItemInMainHand(itemStack);
            return;
        }
        int amount = player.getInventory().getItemInMainHand().getAmount();
        itemStack.setAmount(amount);
        player.getInventory().setItemInMainHand(itemStack);
    }

    private void loadCustomItemOptions(){
        List<ItemOptions> options = customItemType.getAllOptions();

        for (int i = 0; i < OPTION_SLOTS.size(); i++) {

            if (i >= options.size()) continue;

            ItemOptions option = options.get(i);

            ItemComponent component =
                    baseCustomItem.getComponent(option.asComponentClass());

            Object value = (component instanceof EditableComponent<?> ec)
                    ? ec.getValue()
                    : "N/A";

            ItemBuilder item = new ItemBuilder(
                    option.getIcon(),
                    "<gold>" + option.getDisplayName()
            );

            item.addLore("<yellow>Current Value: <gray>" + value);
            item.addLore("<gray>Click to edit");

            inventory.setItem(OPTION_SLOTS.get(i), item.build());
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        super.onClick(e);
        e.setCancelled(true);

        int slot = e.getSlot();

        // registry button
        if (slot == REGISTRY_SAVE_SLOT && e.isLeftClick()) {
            new ConfirmationGui(player, this, p -> {
                registry.saveCustomItem(baseCustomItem);
                reopen();
            }).open();
            return;
        }

        int index = OPTION_SLOTS.indexOf(slot);
        if (index == -1) return;

        List<ItemOptions> options = customItemType.getAllOptions();
        if (index >= options.size()) return;

        ItemOptions option = options.get(index);

        ItemComponent component =
                baseCustomItem.getComponent(option.asComponentClass());

        if (!(component instanceof EditableComponent<?> editable)) return;

        // forward ALL logic to component
        editable.openEditor(player, baseCustomItem, this::reopen);
    }

    private void openEnumOptionGui(Type type, ItemOptions option) {

        new OptionsGui(player, type, selected -> {

            if (option.isComponent()) {

                ItemComponent component =
                        baseCustomItem.getComponent(option.asComponentClass());

                if (component instanceof EditableComponent<?> editable) {
                    applyToComponent(editable, selected);
                }

            } else {
                applyCoreValue(option, selected);
            }

            if (type == CustomItemType.class) {
                this.customItemType = (CustomItemType) selected;
                buildNewItemType();
            }

            updateItem();

        }, baseCustomItem, inputListener, this).open();
    }

    @SuppressWarnings("unchecked")
    private static <T> void applyToComponent(EditableComponent<T> component, Object value) {
        if (component == null) return;

        component.setValue((T) value);
    }

    private void applyCoreValue(ItemOptions option, Object value) {

        switch (option) {

            case MATERIAL ->
                    baseCustomItem.setMaterial((Material) value);

            case MINEFINITY_ID ->
                    baseCustomItem.setId((String) value);

            case DISPLAY_NAME ->
                    baseCustomItem.setDisplayName((String) value);

            case RARITY ->
                    baseCustomItem.setRarity((Rarity) value);

            case CUSTOM_ITEM_TYPE -> {
                baseCustomItem.setItemType((CustomItemType) value);
                this.customItemType = (CustomItemType) value;
                buildNewItemType();
            }
        }
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
