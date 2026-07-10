package org.evasive.me.minefinity.customItems.registry.service;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.config.RegistryConfigHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class CustomItemRegistryService {

    private final CustomItemRegistry customItemRegistry;
    private final RegistryConfigHandler registryConfigHandler;

    private static CustomItemRegistryService instance;

    public CustomItemRegistryService(CustomItemRegistry customItemRegistry, RegistryConfigHandler registryConfigHandler) {
        this.customItemRegistry = customItemRegistry;
        this.registryConfigHandler = registryConfigHandler;
    }

    public static void init(CustomItemRegistry customItemRegistry,
                            RegistryConfigHandler registryConfigHandler) {
        instance = new CustomItemRegistryService(customItemRegistry, registryConfigHandler);
    }

    public static CustomItemRegistryService get() {
        if (instance == null) {
            throw new IllegalStateException("CustomItemRegistryService not initialized!");
        }
        return instance;
    }

    public boolean isRegistered(String itemId) {
        return customItemRegistry.isRegistered(itemId);
    }

    public boolean isRegistered(ItemStack itemStack) {
        if(itemStack == null || itemStack.isEmpty() || !itemStack.hasItemMeta()) return false;
        String itemId = getItemId(itemStack);
        if(itemId == null) return false;
        return customItemRegistry.isRegistered(itemId);
    }

    public String getItemId(ItemStack itemStack){
        if(itemStack == null || itemStack.isEmpty() || !itemStack.hasItemMeta()) return null;
        return itemStack.getItemMeta().getPersistentDataContainer().get(ITEM_ID_KEY, PersistentDataType.STRING);
    }

    public BaseCustomItem getRegisteredBaseItem(String itemId){
        if(!customItemRegistry.isRegistered(itemId)){
            return new BaseCustomItem(itemId, Material.YELLOW_STAINED_GLASS, itemId, Rarity.MINOR);
        }
        return customItemRegistry.getByID(itemId).copy();
    }

    public BaseCustomItem getRegisteredBaseItem(ItemStack itemStack){
        if(itemStack == null || itemStack.isEmpty())
            return null;
        String id = getItemId(itemStack);
        if(id == null || !customItemRegistry.isRegistered(id))
            return null;

        BaseCustomItem item = customItemRegistry.getByID(id).copy();
        PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();

        for (ItemComponent c : item.getComponents())
            if (c.isInstanceData()) c.load(pdc);

        return item;
    }

    public BaseCustomItem getBaseItemById(String itemId){
        if(itemId == null) {
            return null;
        }
        if(!customItemRegistry.isRegistered(itemId)){
            return getUnregisteredItem(itemId);
        }
        return customItemRegistry.getByID(itemId).copy();
    }

    public ItemStack getRegisteredItemStack(String itemId){

        if(!customItemRegistry.isRegistered(itemId))
            return getUnregisteredItem(itemId).buildItem().clone();
        BaseCustomItem baseCustomItem = getRegisteredBaseItem(itemId);
        if(baseCustomItem instanceof BasePickaxeItem pickaxe)
            return pickaxe.buildItem();
        return customItemRegistry.getByID(itemId).buildItem();

    }

    public void saveCustomItem(BaseCustomItem baseCustomItem){
        registryConfigHandler.addSingleEntry(baseCustomItem);
        customItemRegistry.overrideCustomItem(baseCustomItem);
    }

    public void removeRegisteredItem(String itemId) {
        customItemRegistry.removeCustomItem(itemId);
    }

    public Set<String> getAllItemIds() {
        return customItemRegistry.getAllItemIDs();
    }

    private BaseCustomItem getUnregisteredItem(String itemId){
        return new BaseCustomItem(itemId, Material.YELLOW_STAINED_GLASS_PANE, itemId, Rarity.MINOR);
    }

    public ItemStack rebuildItem(ItemStack itemStack){
        BaseCustomItem baseCustomItem = getRegisteredBaseItem(itemStack);
        ItemStack rebuiltItemStack;
        if (baseCustomItem instanceof BasePickaxeItem pickaxe) {
            rebuiltItemStack = pickaxe.buildItem();
        } else {
            rebuiltItemStack = baseCustomItem.buildItem();
        }
        rebuiltItemStack.setAmount(itemStack.getAmount());
        return rebuiltItemStack.clone();
    }

    public ItemStack buildItem(BaseCustomItem item) {
        if (item instanceof BasePickaxeItem pickaxe) {
            return pickaxe.buildItem();
        }
        return item.buildItem();
    }

    public boolean isPickaxe(ItemStack itemStack) {
        String id = getItemId(itemStack);
        if (id == null || !customItemRegistry.isRegistered(id)) return false;
        return customItemRegistry.getByID(id).getCustomItemType() == CustomItemType.PICKAXE;
    }

    public List<BasePartItem> getCompatibleParts(PartSlots category, CustomItemType toolType){
        List<BasePartItem> compatiblePartList = new ArrayList<>();

        for(BaseCustomItem baseCustomItem : customItemRegistry.getAllItems()){

            if(baseCustomItem.getCustomItemType() != CustomItemType.TOOL_PART)
                continue;

            BasePartItem partItem = (BasePartItem) baseCustomItem;

            if(partItem.fits(category, toolType))
                compatiblePartList.add(partItem);
        }
        return compatiblePartList;
    }
}
