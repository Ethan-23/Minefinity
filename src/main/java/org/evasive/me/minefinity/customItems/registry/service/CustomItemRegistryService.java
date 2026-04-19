package org.evasive.me.minefinity.customItems.registry.service;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.core.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.itembuilder.resolvers.PickaxeResolver;
import org.evasive.me.minefinity.customItems.itembuilder.service.PickaxeService;
import org.evasive.me.minefinity.customItems.registry.config.RegistryConfigHandler;

import java.util.Set;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class CustomItemRegistryService {

    private final CustomItemRegistry customItemRegistry;
    private final RegistryConfigHandler registryConfigHandler;
    private final PickaxeResolver pickaxeResolver;
    private final PickaxeService pickaxeService;

    private static CustomItemRegistryService instance;

    public CustomItemRegistryService(CustomItemRegistry customItemRegistry, RegistryConfigHandler registryConfigHandler) {
        this.customItemRegistry = customItemRegistry;
        this.registryConfigHandler = registryConfigHandler;

        this.pickaxeResolver = new PickaxeResolver(this);
        this.pickaxeService = new PickaxeService(pickaxeResolver);
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

    public String getItemType(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().get(ITEM_TYPE_KEY, PersistentDataType.STRING);
    }

    public BaseCustomItem getRegisteredBaseItem(String itemId){
        if(!customItemRegistry.isRegistered(itemId)){
            return new BaseCustomItem(itemId, Material.YELLOW_STAINED_GLASS, itemId, Rarity.MINOR);
        }
        return customItemRegistry.getByID(itemId).getBaseItem().copy();
    }

    public BaseCustomItem getRegisteredBaseItem(ItemStack itemStack){
        if(itemStack == null || itemStack.isEmpty())
            return null;
        String itemId = getItemId(itemStack);

        if(itemId == null || !customItemRegistry.isRegistered(itemId))
            return null;

        BaseCustomItem baseCustomItem = customItemRegistry.getByID(itemId).getBaseItem().copy();

        if(!(baseCustomItem instanceof BasePickaxeItem basePickaxeItem))
            return baseCustomItem;

        PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
        basePickaxeItem.setPickaxeHeadId(pdc.get(PICKAXE_HEAD_KEY, PersistentDataType.STRING));
        basePickaxeItem.setPickaxeCoreId(pdc.get(PICKAXE_CORE_KEY, PersistentDataType.STRING));
        basePickaxeItem.setPickaxeHandleId(pdc.get(PICKAXE_HANDLE_KEY, PersistentDataType.STRING));
        return basePickaxeItem;
    }

    public BaseCustomItem getBaseItemById(String itemId){
        if(itemId == null) {
            return null;
        }
        if(!customItemRegistry.isRegistered(itemId)){
            return getUnregisteredItem(itemId);
        }
        return customItemRegistry.getByID(itemId).getBaseItem().copy();
    }

    public ItemStack getRegisteredItemStack(String itemId){

        if(!customItemRegistry.isRegistered(itemId))
            return getUnregisteredItem(itemId).buildItem().clone();
        BaseCustomItem baseCustomItem = getRegisteredBaseItem(itemId);
        if(baseCustomItem instanceof BasePickaxeItem pickaxe)
            return pickaxeService.buildItem(pickaxe);
        return customItemRegistry.getByID(itemId).getBaseItem().buildItem();

    }

    public static String getStringPDC(ItemStack itemStack, NamespacedKey namespacedKey){
        return itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
    }

    public void saveCustomItem(CustomItem customItem){
        registryConfigHandler.addSingleEntry((BaseCustomItem) customItem);
        customItemRegistry.overrideCustomItem(customItem);
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
            rebuiltItemStack = pickaxeService.buildItem(pickaxe);
        } else {
            rebuiltItemStack = baseCustomItem.buildItem();
        }
        rebuiltItemStack.setAmount(itemStack.getAmount());
        return rebuiltItemStack.clone();
    }

    public ItemStack buildItem(BaseCustomItem item) {
        if (item instanceof BasePickaxeItem pickaxe) {
            return pickaxeService.buildItem(pickaxe);
        }
        return item.buildItem();
    }

    public PickaxeResolver getPickaxeResolver() {
        return pickaxeResolver;
    }
}
