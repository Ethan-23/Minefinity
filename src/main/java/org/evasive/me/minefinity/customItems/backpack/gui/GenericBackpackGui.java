package org.evasive.me.minefinity.customItems.backpack.gui;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.backpack.BackpackHandler;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.types.BaseBackpackItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.List;

import static org.evasive.me.minefinity.core.utils.TextConversions.formatItemName;
import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.fillerPane;

public class GenericBackpackGui extends BaseGui {

    public int insertButton;
    private final int inventorySize;
    private final String backpackId;
    BackpackGuiHandler backpackGuiHandler;

    private final CustomItemRegistryService customItemRegistryService;
    private final BackpackService backpackService;

    public GenericBackpackGui(Player player, String backpackId, CustomItemRegistryService customItemRegistryService, BackpackService backpackService) {
        super(player, new BackpackGuiHandler(customItemRegistryService, backpackService).calculateBackpackSize(backpackId), TextConversions.parse(backpackId));
        backpackGuiHandler = new BackpackGuiHandler(customItemRegistryService, backpackService);
        this.customItemRegistryService = customItemRegistryService;
        this.backpackService = backpackService;
        inventorySize = backpackGuiHandler.calculateBackpackSize(backpackId);
        insertButton = inventorySize - 5;
        this.backpackId = backpackId;
        build();
    }

    @Override
    protected void build() {
        List<String> storedItemIdList = ((BaseBackpackItem)customItemRegistryService.getBaseItemById(backpackId)).storageListComponent().getValue();
        List<String> sortedStoredItemIdList = storedItemIdList.stream().sorted().toList();
        for(int i = 0; i < sortedStoredItemIdList.size(); i++){
            inventory.setItem(i, createItemStorage(backpackId, player, sortedStoredItemIdList.get(i)));
        }
        createBottomArea();
    }


    private void createBottomArea() {
        for(int i = inventorySize - 1; i > inventorySize - 10; i--){
            inventory.setItem(i, fillerPane);
        }
        inventory.setItem(insertButton, new CustomItemBuilder(Material.CHEST, "<green>Insert Inventory").build());
    }

    private ItemStack createItemStorage(String backpackId, Player player, String itemId){
        ItemStack customItem = customItemRegistryService.getRegisteredItemStack(itemId);
        int amount = backpackService.getBackpackStoredItemAmount(player, itemId);
        int totalStorage = new BackpackHandler(customItemRegistryService, backpackService).getTotalBackpackStorage(player, backpackId);
        CustomItemBuilder storageBlock = new CustomItemBuilder(customItem).setLore(
                List.of(
                        "<gray>" + formatItemName(backpackId),
                        "",
                        "<gray>Stored: <#555555>" + amount + "<gray>/" + totalStorage,
                        ""
                )
        );
        if(amount == 0)
            return storageBlock.addLore("<#555555>Empty backpack!").build();
        return storageBlock.addLore("<#55FFFF>Right-Click for stack!")
                .addLore("<yellow>Click to pickup!").build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);

        String backpackId = PlainTextComponentSerializer.plainText().serialize(e.getView().title());

        Player player = (Player) e.getWhoClicked();

        if(e.getSlot() == insertButton){
            e.setCancelled(true);
            backpackGuiHandler.handleInsertAll(player, backpackId);
            rebuildInventory();
            return;
        }

        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();

        if(!customItemRegistryService.isRegistered(item)) return;

        assert item != null;
        String itemId = customItemRegistryService.getItemId(item);

        if(e.getClick().isLeftClick()){
            backpackGuiHandler.handleTakeAll(player, itemId);
        }else if(e.getClick().isRightClick()){
            backpackGuiHandler.handleTakeStack(player, itemId);
        }

        rebuildInventory();
    }
}
