package org.evasive.me.minevolutionCore.customItems.backpack.gui;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.core.gui.BaseGui;
import org.evasive.me.minevolutionCore.customItems.CustomItemRegistry;
import org.evasive.me.minevolutionCore.customItems.backpack.Backpacks;
import org.evasive.me.minevolutionCore.customItems.backpack.BackpackCollect;
import org.evasive.me.minevolutionCore.utils.ItemBuilder;
import org.evasive.me.minevolutionCore.utils.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.getItemId;
import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.hasItemId;
import static org.evasive.me.minevolutionCore.customItems.ItemNameBuilder.formatItemName;
import static org.evasive.me.minevolutionCore.utils.GenericGuiItems.fillerPane;

public class GenericBackpackGui extends BaseGui {

    public int insertButton;
    private final int inventorySize;
    private final String backpackId;
    BackpackGuiHandler backpackGuiHandler;

    public GenericBackpackGui(Player player, String backpackId) {
        super(player, new BackpackGuiHandler().calculateBackpackSize(backpackId), Messages.parse(backpackId));
        backpackGuiHandler = new BackpackGuiHandler();
        inventorySize = backpackGuiHandler.calculateBackpackSize(backpackId);
        insertButton = inventorySize - 5;
        this.backpackId = backpackId;
        build();
    }

    @Override
    protected void build() {
        Set<String> storedItemIdList = Backpacks.valueOf(backpackId).getBuilder().getStoredItemIdList();
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
        inventory.setItem(insertButton, new ItemBuilder(Material.CHEST, Messages.parse("<green>Insert Inventory")).build());
    }

    private ItemStack createItemStorage(String backpackId, Player player, String itemId){
        ItemStack customItem = CustomItemRegistry.getByID(itemId).getBuilder().buildItem();
        int amount = MinevolutionCore.playerManager.getBackpackStoredItemAmount(player, itemId);
        int totalStorage = new BackpackCollect().getTotalBackpackStorage(player, backpackId);
        ItemBuilder storageBlock = new ItemBuilder(customItem).setLore(
                List.of(
                        Messages.parse("<gray>" + formatItemName(backpackId)),
                        Messages.parse(""),
                        Messages.parse("<gray>Stored: <#555555>" + amount + "<gray>/" + totalStorage),
                        Messages.parse("")
                )
        );
        if(amount == 0)
            return storageBlock.addLore("<#555555>Empty backpack!").build();
        return storageBlock.addLore("<#55FFFF>Right-Click for stack!")
                .addLore("<yellow>Click to pickup!").build();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
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

        if(!hasItemId(item)) return;

        String itemId = getItemId(item);

        if(e.getClick().isLeftClick()){
            backpackGuiHandler.handleTakeAll(player, itemId);
        }else if(e.getClick().isRightClick()){
            backpackGuiHandler.handleTakeStack(player, itemId);
        }

        rebuildInventory();
    }
}
