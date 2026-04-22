package org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil.gui.PickaxeAnvilGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeComponent;

public class PickaxeAnvilHandler {

    private final CustomItemRegistryService customItemRegistryService;

    public PickaxeAnvilHandler(CustomItemRegistryService customItemRegistryService) {
        this.customItemRegistryService = customItemRegistryService;
    }

    public boolean isCorrectComponentType(ItemStack itemStack, CustomItemType slotType) {

        if(itemStack == null || itemStack.isEmpty())
            return false;

        if(!customItemRegistryService.isRegistered(itemStack))
            return false;

        BaseCustomItem baseCustomItem = new BaseCustomItem(itemStack);

        return baseCustomItem.getCustomItemType() == slotType;
    }


    public boolean verifyPartChange(ItemStack pickaxe, ItemStack cursorItem, ItemStack currentItem, CustomItemType itemType){

        if(!(customItemRegistryService.getRegisteredBaseItem(pickaxe) instanceof BasePickaxeItem))
            return false;

        boolean correctItemTypeCursor = isCorrectComponentType(cursorItem, itemType);
        boolean correctItemTypeCurrent = isCorrectComponentType(currentItem, itemType);

        if (!correctItemTypeCursor && !correctItemTypeCurrent) return false;

        if(correctItemTypeCursor && !isCorrectTemplateTier(pickaxe, cursorItem)) return false;

        if(!correctItemTypeCursor && !cursorItem.isEmpty()) return false;

        return true;
    }

    public boolean isCorrectTemplateTier(ItemStack pickaxe, ItemStack pickaxePart) {
        BasePickaxeComponent basePickaxeComponent = (BasePickaxeComponent) customItemRegistryService.getRegisteredBaseItem(pickaxePart);
        BasePickaxeItem basePickaxe = (BasePickaxeItem) customItemRegistryService.getRegisteredBaseItem(pickaxe);
        int pickaxeComponentTier = basePickaxeComponent.getStatAmount(Stats.BREAKING_POWER);
        int pickaxeTier = basePickaxe.getPickaxeTier();
        return pickaxeTier >= pickaxeComponentTier;
    }


    public void handlePickaxeSlot(InventoryClickEvent e) {

        PickaxeAnvilGui pickaxeAnvilGui = (PickaxeAnvilGui) e.getInventory().getHolder();
        assert pickaxeAnvilGui != null;

        ItemStack cursorItem = e.getCursor();
        ItemStack currentItem = e.getCurrentItem();

        boolean cursorPickaxe = (customItemRegistryService.getRegisteredBaseItem(cursorItem) instanceof BasePickaxeItem);
        boolean currentPickaxe = (customItemRegistryService.getRegisteredBaseItem(currentItem) instanceof BasePickaxeItem);


        if(currentItem != null && !cursorItem.isEmpty() && !cursorPickaxe && currentPickaxe){
            e.setCancelled(true);
            return;
        }

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
        String headId = customItemRegistryService.getItemId(head);
        String coreId = customItemRegistryService.getItemId(core);
        String handleId = customItemRegistryService.getItemId(handle);
        basePickaxeItem.setPickaxeHeadId(customItemRegistryService.isRegistered(headId) ? headId : null);
        basePickaxeItem.setPickaxeCoreId(customItemRegistryService.isRegistered(coreId) ? coreId : null);
        basePickaxeItem.setPickaxeHandleId(customItemRegistryService.isRegistered(handleId) ? handleId : null);
        return customItemRegistryService.buildItem(basePickaxeItem);
    }

}
