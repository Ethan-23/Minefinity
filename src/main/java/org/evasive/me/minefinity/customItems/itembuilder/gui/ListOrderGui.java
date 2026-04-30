package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;

import java.util.Collections;
import java.util.List;

import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.backPage;

public class ListOrderGui extends BaseGui {

    private final static int INVENTORY_SIZE = 54;
    private final static int BACK_SLOT = 45;
    private final static List<Integer> OPTIONS_SLOTS = List.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

    private final List<?> editableList;
    private final ItemCreationGui itemCreationGui;

    protected ListOrderGui(Player player, List<?> editableList, ItemCreationGui itemCreationGui) {
        super(player, INVENTORY_SIZE, TextConversions.parse("List Order"));
        this.editableList = editableList;
        this.itemCreationGui = itemCreationGui;
        build();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        buildBackSlot();
        loadOptions();
    }

    private void buildBackSlot(){
        inventory.setItem(BACK_SLOT, backPage);
    }

    private void loadOptions(){
        for(int i = 0; i < editableList.size(); i++){
            Object obj = editableList.get(i);
            ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK, obj.toString());
            itemBuilder.addLore("<yellow>Left Click <gray>to move to the left");
            itemBuilder.addLore("<yellow>Right Click <gray>to move to the right");
            itemBuilder.addLore("<yellow>Shift-Right Click <gray>to remove");
            inventory.setItem(OPTIONS_SLOTS.get(i), itemBuilder.build());
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);

        int slot = e.getSlot();

        if(slot == BACK_SLOT){
            itemCreationGui.reopen();
        }

        int index = OPTIONS_SLOTS.indexOf(slot);
        if (index == -1)
            return;

        if(e.getClick().isLeftClick() && index > 0){
            Collections.swap(editableList, index-1, index);
        }else if(e.getClick() == ClickType.SHIFT_RIGHT){
            editableList.remove(index);
        }else if(e.getClick().isRightClick() && index < editableList.size() - 1){
            Collections.swap(editableList, index, index+1);
        }

        itemCreationGui.updateItem();
        rebuildInventory();
    }
}
