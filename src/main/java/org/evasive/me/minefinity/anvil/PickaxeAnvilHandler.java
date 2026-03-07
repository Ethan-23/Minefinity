package org.evasive.me.minefinity.anvil;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.anvil.gui.PickaxeAnvilGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.BasePickaxeComponent;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.*;

public class PickaxeAnvilHandler {

    public static boolean isCorrectComponentType(ItemStack itemStack, CustomItemType slotType) {

        if(!hasItemId(itemStack))
            return false;

        BaseCustomItem baseCustomItem = new BaseCustomItem(itemStack);

        return baseCustomItem.getCustomItemType() == slotType;
    }


    public boolean verifyPartChange(ItemStack pickaxe, ItemStack cursorItem, ItemStack currentItem, CustomItemType itemType){


        if(!(getRegisteredItem(pickaxe) instanceof BasePickaxeItem))
            return false;

        boolean correctItemTypeCursor = isCorrectComponentType(cursorItem, itemType);
        boolean correctItemTypeCurrent = isCorrectComponentType(currentItem, itemType);

        if (!correctItemTypeCursor && !correctItemTypeCurrent) return false;

        if(correctItemTypeCursor && !isCorrectTemplateTier(pickaxe, cursorItem)) return false;

        if(!correctItemTypeCursor && !cursorItem.isEmpty()) return false;

        return true;
    }

    public boolean isCorrectTemplateTier(ItemStack pickaxe, ItemStack pickaxePart) {
        BasePickaxeComponent basePickaxeComponent = (BasePickaxeComponent) CustomItemRegistry.getByID(getItemId(pickaxePart));
        BasePickaxeItem basePickaxe = (BasePickaxeItem) CustomItemRegistry.getByID(getItemId(pickaxe));
        int pickaxeComponentTier = basePickaxeComponent.getRequiredPickaxeTier();
        int pickaxeTier = basePickaxe.getPickaxeTier();
        return pickaxeTier >= pickaxeComponentTier;
    }


    public void handlePickaxeSlot(InventoryClickEvent e) {

        PickaxeAnvilGui pickaxeAnvilGui = (PickaxeAnvilGui) e.getInventory().getHolder();
        assert pickaxeAnvilGui != null;

        ItemStack cursorItem = e.getCursor();
        ItemStack currentItem = e.getCurrentItem();

        boolean cursorPickaxe = (getRegisteredItem(cursorItem) instanceof BasePickaxeItem);
        boolean currentPickaxe = (getRegisteredItem(currentItem) instanceof BasePickaxeItem);

        if (!cursorPickaxe && !currentPickaxe) {
            e.setCancelled(true);
            return;
        }

        if (cursorPickaxe) {
            pickaxeAnvilGui.addPickaxeParts(cursorItem);
            return;
        }

        pickaxeAnvilGui.resetSlots();
    }

    /*public ItemStack updatePickaxeItem(ItemStack pickaxe, ItemStack head, ItemStack core, ItemStack handle){
        BasePickaxeItem basePickaxeItem = new BasePickaxeItem(pickaxe);
        basePickaxeItem.setPickaxeHead(hasItemId(head) ? getItemId(head) : null);
        basePickaxeItem.setPickaxeCore(hasItemId(core) ? getItemId(core) : null);
        basePickaxeItem.setPickaxeHandle(hasItemId(handle) ? getItemId(handle) : null);
        return basePickaxeItem.createBasePickaxe();
    }*/

}
