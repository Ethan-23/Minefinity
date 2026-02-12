package org.evasive.me.minefinity.customItems;

import org.evasive.me.minefinity.core.items.CustomItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomItemRegistry {

    private static final Map<String, CustomItem> ITEMS_BY_ID = new HashMap<>();

    /**
     * Registers all items in list to the ITEMS_BY_ID map.
     * @param items item list being added.
     */
    public static void registerEnumItems(CustomItem[] items) {
        for (CustomItem item : items) {
            registerCustomItem(item);
        }
    }

    /**
     * Adds custom item to the ITEMS_BY_ID map.
     * @param item item being added to the map.
     */
    public static void registerCustomItem(CustomItem item) {
        if (ITEMS_BY_ID.containsKey(item.getID())) {
            throw new IllegalArgumentException("Item with ID '" + item.getID() + "' is already registered!");
        }

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

}
