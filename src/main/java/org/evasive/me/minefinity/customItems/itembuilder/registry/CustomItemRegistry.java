package org.evasive.me.minefinity.customItems.itembuilder.registry;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.customItems.itembuilder.data.*;
import org.evasive.me.minefinity.customItems.itembuilder.registry.config.ItemRegistryConfigManager;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.rarity.Rarity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.*;

public class CustomItemRegistry {

    private static final Map<String, CustomItem> ITEMS_BY_ID = new HashMap<>();


    public static void init(){
        for (CustomItem item : getAllItems()) {
            item.getBaseItem().buildItem();
        }
    }

    /**
     * Adds new custom item to the ITEMS_BY_ID map.
     * @param item item being added to the map.
     */
    public static void registerCustomItem(CustomItem item) {
        if (ITEMS_BY_ID.containsKey(item.getID())) {
            throw new IllegalArgumentException("Item with ID '" + item.getID() + "' is already registered!");
        }

        ITEMS_BY_ID.put(item.getID(), item);
    }

    /**
     * Adds custom item to the ITEMS_BY_ID map.
     * @param item item being added to the map.
     */
    public static void overrideCustomItem(CustomItem item) {
        ITEMS_BY_ID.put(item.getID(), item);
    }

    /**
     * Gets CustomItem by given id string.
     * @param id item id string.
     * @return returns the CustomItem type of item id.
     */
    public static CustomItem getByID(String id) {
        return ITEMS_BY_ID.get(id);
    }

    /**
     * Gets all item ids in the map.
     * @return Set of string ids of all items.
     */
    public static Set<String> getAllItemIDs() {
        return ITEMS_BY_ID.keySet(); // already uppercased
    }

    /**
     * Gets a collection of all items.
     * @return Collection of all CustomItem types.
     */
    public static Collection<CustomItem> getAllItems() {
        return ITEMS_BY_ID.values();
    }

    /**
     * Checks if a given item id is already registered to the map.
     * @param id Id of item being checked.
     * @return True if registered. False if not.
     */
    public static boolean isRegistered(String id) {
        return ITEMS_BY_ID.containsKey(id);
    }

    public static void removeCustomItem(String id) {
        ITEMS_BY_ID.remove(id);
    }

    public static void registerConfigItems(ItemRegistryConfigManager itemRegistryConfigManager) {

        ConfigurationSection itemsSection = itemRegistryConfigManager
                .getItemRegistryConfig()
                .getConfigurationSection("items");

        if(itemsSection == null) return;

        for(String itemID : itemsSection.getKeys(false)) {
            ConfigurationSection customItemSection = itemsSection.getConfigurationSection(itemID);

            if(customItemSection == null) continue;

            Material material = Material.valueOf(customItemSection.getString("material"));
            String displayName = customItemSection.getString("display_name");
            Rarity rarity = Rarity.valueOf(customItemSection.getString("rarity"));

            CustomItemType customItemType = CustomItemType.valueOf(customItemSection.getString("custom_item_type"));
            BaseCustomItem baseCustomItem = customItemType.create(itemID, material, displayName, rarity);

            if(customItemSection.get("sell_value") != null)
                baseCustomItem.setValue((float) customItemSection.getDouble("sell_value"));
            if(customItemSection.get("visual_material") != null)
                baseCustomItem.setVisualMaterial(Material.valueOf(customItemSection.getString("visual_material")));
            if(customItemSection.get("flavor_text") != null)
                baseCustomItem.setFlavorText(customItemSection.getString("flavor_text"));
            if(customItemSection.get("glowing") != null)
                baseCustomItem.setGlowing(customItemSection.getBoolean("glowing"));
            if(customItemSection.get("soulbound") != null)
                baseCustomItem.setSoulbound(customItemSection.getBoolean("soulbound"));
            if(customItemSection.get("stack_size") != null)
                baseCustomItem.setStackSize(customItemSection.getInt("stack_size"));


            if(baseCustomItem instanceof BasePickaxeItem){
                ((BasePickaxeItem) baseCustomItem).setBaseMiningSpeed((float) customItemSection.getDouble("mining_speed"));
                ((BasePickaxeItem) baseCustomItem).setPickaxeHeadId(customItemSection.getString("pickaxe_head"));
                ((BasePickaxeItem) baseCustomItem).setPickaxeCoreId(customItemSection.getString("pickaxe_core"));
                ((BasePickaxeItem) baseCustomItem).setPickaxeHandleId(customItemSection.getString("pickaxe_handle"));
            }else if(baseCustomItem instanceof BasePickaxeComponent){
                for (String string : customItemSection.getStringList("pickaxe_abilities")) {
                    ((BasePickaxeComponent) baseCustomItem).changePickaxeAbilityList(PickaxeAbilities.valueOf(string));
                }
            }else if(baseCustomItem instanceof BaseFuelItem){
                ((BaseFuelItem) baseCustomItem).setFuelAmount(customItemSection.getInt("fuel_amount"));
            }else if(baseCustomItem instanceof BaseBackpackItem){
                ((BaseBackpackItem) baseCustomItem).setStoredItemAmount(customItemSection.getInt("storage_amount"));
                for (String string : customItemSection.getStringList("storage_list")) {
                    ((BaseBackpackItem) baseCustomItem).changeStoredItemIdList(string);
                }
            }


            ITEMS_BY_ID.put(itemID, baseCustomItem);
        }
    }
}
