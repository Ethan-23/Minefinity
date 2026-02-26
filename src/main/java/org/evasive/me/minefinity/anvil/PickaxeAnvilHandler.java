package org.evasive.me.minefinity.anvil;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.anvil.gui.PickaxeAnvilGui;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeComponent;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.*;
import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.getPickaxeComponent;
import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.isPickaxe;

public class PickaxeAnvilHandler {

    public static boolean isCorrectComponentType(ItemStack itemStack, CustomItemType slotType) {
        if(!hasItemId(itemStack))
            return false;

        if(!PickaxeComponent.contains(getItemId(itemStack))) return false;

        PickaxeComponent component = PickaxeComponent.valueOf(getItemId(itemStack));
        return component.getBuilder().getItemType().equals(slotType);
    }


    public boolean verifyPartChange(ItemStack pickaxe, ItemStack cursorItem, ItemStack currentItem, CustomItemType itemType){

        if(!isPickaxe(pickaxe)){
            return false;
        }

        boolean correctItemTypeCursor = isCorrectComponentType(cursorItem, itemType);
        boolean correctItemTypeCurrent = isCorrectComponentType(currentItem, itemType);

        if (!correctItemTypeCursor && !correctItemTypeCurrent) return false;

        if(correctItemTypeCursor && !isCorrectTemplateTier(pickaxe, cursorItem)) return false;

        if(!correctItemTypeCursor && !cursorItem.isEmpty()) return false;

        return true;
    }

    public boolean isCorrectTemplateTier(ItemStack pickaxe, ItemStack pickaxePart) {
        BasePickaxeComponent basePickaxeComponent = getPickaxeComponent(pickaxePart);
        PickaxeItem partTemplateTier = basePickaxeComponent.getMinimumRequiredTemplateTier();
        PickaxeItem pickaxeTemplateTier = PickaxeItem.valueOf(getItemId(pickaxe));
        return partTemplateTier.ordinal() <= pickaxeTemplateTier.ordinal();
    }


    public void handlePickaxeSlot(InventoryClickEvent e) {

        PickaxeAnvilGui pickaxeAnvilGui = (PickaxeAnvilGui) e.getInventory().getHolder();
        assert pickaxeAnvilGui != null;

        ItemStack cursorItem = e.getCursor();
        ItemStack currentItem = e.getCurrentItem();

        boolean cursorPickaxe = isPickaxe(cursorItem);
        boolean currentPickaxe = isPickaxe(currentItem);

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

    public ItemStack updatePickaxeItem(ItemStack pickaxe, ItemStack head, ItemStack core, ItemStack handle){
        BasePickaxeItem basePickaxeItem = new BasePickaxeItem(pickaxe);
        basePickaxeItem.setPickaxeHead(hasItemId(head) ? PickaxeComponent.valueOf(getItemId(head)) : null);
        basePickaxeItem.setPickaxeCore(hasItemId(core) ? PickaxeComponent.valueOf(getItemId(core)) : null);
        basePickaxeItem.setPickaxeHandle(hasItemId(handle) ? PickaxeComponent.valueOf(getItemId(handle)) : null);
        return basePickaxeItem.createBasePickaxe();
    }

}
