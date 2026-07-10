package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.ConfirmationGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ItemOptions;
import org.evasive.me.minefinity.customItems.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.EditableComponent;
import org.evasive.me.minefinity.core.events.PlayerInputListener;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.List;

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
                           PlayerInputListener inputListener,
                           CustomItemRegistryService registry) {
        super(player, INVENTORY_SIZE, TextConversions.parse("CustomItemBuilder"));

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
        inventory.setItem(BUILT_ITEM_SLOT, registry.buildItem(baseCustomItem));
    }

    public void addRegistryButton() {
        boolean registered = registry.isRegistered(baseCustomItem.getID());
        Material pane = registered ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE;
        String save = registered ? "<bold><yellow>Override" : "<bold><green>Save";
        String text = registered
                ? "<yellow>Left Click <gray>to <bold><yellow>OVERRIDE <reset><gray>in the item registry. This will update all items with this id globally."
                : "<green>Left Click <gray>to <bold><green>save <reset><gray>to the item registry.";
        CustomItemBuilder saveButton = new CustomItemBuilder(pane, TextConversions.parse(save));
        saveButton.addBlank().addLore(text);
        inventory.setItem(REGISTRY_SAVE_SLOT, saveButton.build());
    }

    public void updateItem() {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack playerItem = playerInventory.getItemInMainHand();
        ItemStack itemStack = registry.buildItem(baseCustomItem);
        if (playerItem.isEmpty()) {
            playerInventory.setItemInMainHand(itemStack);
            return;
        }
        itemStack.setAmount(playerItem.getAmount());
        playerInventory.setItemInMainHand(itemStack);
    }

    private void loadCustomItemOptions() {
        List<ItemOptions> options = customItemType.getAllOptions();

        for (int i = 0; i < OPTION_SLOTS.size() && i < options.size(); i++) {
            ItemOptions option = options.get(i);

            CustomItemBuilder item = new CustomItemBuilder(option.getIcon(), "<gold>" + option.getDisplayName());
            item.addLore("<yellow>Current: <gray>" + currentValue(option));
            item.addLore("<gray>Click to edit");

            inventory.setItem(OPTION_SLOTS.get(i), item.build());
        }
    }

    private String currentValue(ItemOptions option) {
        if (option.isComponent()) {
            ItemComponent component = baseCustomItem.getComponent(option.asComponentClass());
            if (component instanceof EditableComponent<?> editable) {
                return String.valueOf(editable.getValue());
            }
            return "N/A";
        }

        return switch (option) {
            case MINEFINITY_ID -> baseCustomItem.getID();
            case MATERIAL -> baseCustomItem.getMaterial().name();
            case DISPLAY_NAME -> baseCustomItem.getDisplayName();
            case RARITY -> baseCustomItem.getRarity().name();
            case CUSTOM_ITEM_TYPE -> baseCustomItem.getCustomItemType().name();
            default -> "N/A";
        };
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);

        int slot = e.getSlot();

        if (slot == REGISTRY_SAVE_SLOT && e.isLeftClick()) {
            handleRegistrySave();
            return;
        }

        int index = OPTION_SLOTS.indexOf(slot);
        if (index == -1)
            return;

        List<ItemOptions> options = customItemType.getAllOptions();
        if (index >= options.size())
            return;

        ItemOptions option = options.get(index);

        if (option.isComponent()) {
            ItemComponent component = baseCustomItem.getComponent(option.asComponentClass());
            if (component instanceof EditableComponent<?> editable) {
                editable.openEditor(new EditContext(player, baseCustomItem, inputListener, this));
            }
            return;
        }

        handleCoreOption(option);
    }

    private void handleCoreOption(ItemOptions option) {
        EditContext ctx = new EditContext(player, baseCustomItem, inputListener, this);

        switch (option) {
            case MINEFINITY_ID -> handleId(ctx);

            case DISPLAY_NAME -> ctx.promptString(baseCustomItem::setDisplayName);

            case MATERIAL -> handleMaterial(ctx);

            case RARITY -> handleRarityOption(ctx);

            case CUSTOM_ITEM_TYPE -> handleCustomItemType();

            default -> { }
        }
    }

    private void handleRarityOption(EditContext ctx){
        ctx.openSelector(Rarity.values(), new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(Rarity rarity) {
                CustomItemBuilder icon = new CustomItemBuilder(rarity.getRarityBuilder().material(),
                        TextConversions.buildRarityColor(rarity.name(), rarity));
                if (baseCustomItem.getRarity() == rarity) icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(Rarity rarity, org.bukkit.event.inventory.ClickType click, OptionsGui<Rarity> gui) {
                baseCustomItem.setRarity(rarity);
            }
        });
    }

    private void handleId(EditContext ctx){
        ctx.promptString(input -> {
            if (input.contains(" ")) {
                player.sendMessage(TextConversions.parse("<red>Minefinity ID cannot contain spaces"));
                return;
            }
            baseCustomItem.setId(input.toUpperCase());
        });
    }

    private void handleMaterial(EditContext ctx){
        ctx.promptString(input -> {
            Material material = Material.matchMaterial(input.trim());
            if (material == null || material == Material.AIR) {
                player.sendMessage(TextConversions.parse("<red>Invalid Material Type"));
                return;
            }
            baseCustomItem.setMaterial(material);
        });
    }

    private void handleCustomItemType(){
        new OptionsGui<>(player, List.of(CustomItemType.values()),
                new OptionsGui.OptionAdapter<>() {
                    @Override
                    public ItemStack render(CustomItemType type) {
                        CustomItemBuilder icon = new CustomItemBuilder(type.getDisplayMaterial(),
                                "<gold>" + TextConversions.formatItemName(type.name()));
                        if (baseCustomItem.getCustomItemType() == type) icon.addGlow();
                        return icon.build();
                    }

                    @Override
                    public void onClick(CustomItemType type, org.bukkit.event.inventory.ClickType click, OptionsGui<CustomItemType> gui) {
                        changeItemType(type);
                        reopen();
                    }
                }, this::reopen).open();
    }

    private void handleRegistrySave(){
        new ConfirmationGui(player, this, p -> {
            registry.saveCustomItem(baseCustomItem);
            reopen();
        }).open();
    }

    private void changeItemType(CustomItemType type) {
        baseCustomItem.setItemType(type);
        this.baseCustomItem = type.create(baseCustomItem.buildItem());
        this.customItemType = type;
    }

    public void reopen() {
        updateItem();
        build();
        open();
    }
}
