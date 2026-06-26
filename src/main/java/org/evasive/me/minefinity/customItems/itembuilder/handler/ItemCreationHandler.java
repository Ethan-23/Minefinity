package org.evasive.me.minefinity.customItems.itembuilder.handler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.events.PlayerInputListener;
import org.evasive.me.minefinity.customItems.itembuilder.gui.ItemCreationGui;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.List;
import java.util.Optional;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.PART_SLOT_KEY;
import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.ITEM_TYPE_KEY;

public class ItemCreationHandler {

    private final PlayerInputListener playerInputListener;
    private final CustomItemRegistryService customItemRegistryService;

    public ItemCreationHandler(CustomItemRegistryService customItemRegistryService, PlayerInputListener playerInputListener) {
        this.playerInputListener = playerInputListener;
        this.customItemRegistryService = customItemRegistryService;
    }

    public void handleFloatChanges(Player player, ItemOptions clickedOption, BaseCustomItem baseCustomItem, ItemCreationGui itemCreationGui) {
        player.closeInventory();
        playerInputListener.requestInput(player, input -> {

            if(input.equalsIgnoreCase("cancel")){
                player.sendMessage("<Canceled Selection>");
                itemCreationGui.reopen();
                return;
            }

            float number;
            try {
                number = Float.parseFloat(input);
                //Change to int or round to 2nd number
                if(number < 0){
                    player.sendMessage(TextConversions.parse("<red>Number Must be positive"));
                    itemCreationGui.reopen();
                    return;
                }
            }catch (NumberFormatException exception){
                player.sendMessage(TextConversions.parse("<red>Number is not a float"));
                itemCreationGui.reopen();
                return;
            }
            clickedOption.apply(baseCustomItem, number);
            itemCreationGui.reopen();
        });
    }

    public void handleBooleanChanges(ItemOptions clickedOption, BaseCustomItem baseCustomItem, ItemCreationGui itemCreationGui) {
        Boolean option = ((Optional<Boolean>) clickedOption.get(baseCustomItem)).isPresent();

        if(!option)
            option = true;
        else
            option = null;

        clickedOption.apply(baseCustomItem, option);
        itemCreationGui.reopen();
    }

    public void handleStringChanges(Player player, ItemOptions clickedOption, BaseCustomItem baseCustomItem, ItemStack cursorItem, ItemCreationGui itemCreationGui){

        if(!cursorItem.isEmpty()){
            if(clickedOption == ItemOptions.MATERIAL || clickedOption == ItemOptions.VISUAL_MATERIAL)
                handleMaterialChanges(clickedOption, baseCustomItem, cursorItem);

            if(clickedOption == ItemOptions.STORAGE_LIST)
                handleStorageChange(clickedOption, baseCustomItem, cursorItem);
            else
                handlePickaxePartChanges(clickedOption, baseCustomItem, cursorItem, clickedOption.name());
            itemCreationGui.reopen();
            return;
        }

        player.closeInventory();

        playerInputListener.requestInput(player, input -> {

            if(input.equalsIgnoreCase("cancel")){
                itemCreationGui.reopen();
                return;
            }

            if(clickedOption == ItemOptions.MINEFINITY_ID){
                if(input.contains(" ")){
                    player.sendMessage(TextConversions.parse("<red>Minefinity ID cannot contain spaces"));
                    itemCreationGui.reopen();
                    return;
                }

            }

            if(clickedOption == ItemOptions.MATERIAL || clickedOption == ItemOptions.VISUAL_MATERIAL){

                if(input.equalsIgnoreCase("air")){
                    player.sendMessage(TextConversions.parse("<red>Invalid Material Type"));
                    itemCreationGui.reopen();
                    return;
                }

                try{
                    Material.valueOf(input.toUpperCase());
                }catch (IllegalArgumentException exception){
                    player.sendMessage(TextConversions.parse("<red>Invalid Material Type"));
                    itemCreationGui.reopen();
                    return;
                }
            }

            clickedOption.apply(baseCustomItem, input);
            itemCreationGui.reopen();
        });
    }

    private void handleStorageChange(ItemOptions clickedOption, BaseCustomItem baseCustomItem, ItemStack cursorItem) {
        if(!customItemRegistryService.isRegistered(cursorItem))
            return;

        if(!List.of(CustomItemType.RESOURCE, CustomItemType.FUEL).contains(CustomItemType.valueOf(customItemRegistryService.getItemType(cursorItem))))
            return;

        clickedOption.apply(baseCustomItem, customItemRegistryService.getItemId(cursorItem));
        cursorItem.setAmount(0);
    }

    public void handleMaterialChanges(ItemOptions clickedOption, BaseCustomItem baseCustomItem, ItemStack cursorItem){
        clickedOption.apply(baseCustomItem, cursorItem.getType().name());
        cursorItem.setAmount(0);
    }

    public void handlePickaxePartChanges(ItemOptions clickedOption, BaseCustomItem baseCustomItem, ItemStack cursorItem, String itemSlot){
        ItemMeta meta = cursorItem.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(!isCorrectItemType(pdc, itemSlot))
            return;

        BasePartItem pickaxeComponent = new BasePartItem(cursorItem);
        clickedOption.apply(baseCustomItem, pickaxeComponent.getBaseItem().getID());
        cursorItem.setAmount(0);
    }

    private boolean isCorrectItemType(PersistentDataContainer pdc, String itemSlot){
        return pdc.has(ITEM_TYPE_KEY) && pdc.has(PART_SLOT_KEY) && itemSlot != null && pdc.get(PART_SLOT_KEY, PersistentDataType.STRING).equalsIgnoreCase(itemSlot);
    }

    public void handleInteger(Player player, ItemOptions clickedOption, BaseCustomItem baseCustomItem, ItemCreationGui itemCreationGui) {
        player.closeInventory();
        playerInputListener.requestInput(player, input -> {

            if(input.equalsIgnoreCase("cancel")){
                itemCreationGui.reopen();
                return;
            }

            int number;
            try {
                number = Integer.parseInt(input);
                if(number < 0){
                    player.sendMessage(TextConversions.parse("Number Must be positive"));
                    itemCreationGui.reopen();
                    return;
                }
            }catch (NumberFormatException exception){
                player.sendMessage(TextConversions.parse("Number is not an integer"));
                itemCreationGui.reopen();
                return;
            }
            clickedOption.apply(baseCustomItem, number);
            itemCreationGui.reopen();
        });
    }
}
