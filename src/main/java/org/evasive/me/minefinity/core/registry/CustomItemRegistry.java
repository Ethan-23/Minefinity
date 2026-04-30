package org.evasive.me.minefinity.core.registry;

import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomItemRegistry {

    private final Map<String, CustomItem> ITEMS_BY_ID = new HashMap<>();

    public CustomItemRegistry() {
        for (CustomItem item : getAllItems()) {
            item.getBaseItem().buildItem();
        }
    }

    /**
     * Adds new custom item to the ITEMS_BY_ID map.
     * @param item item being added to the map.
     */
    public void registerCustomItem(CustomItem item) {
        if (ITEMS_BY_ID.containsKey(item.getID())) {
            throw new IllegalArgumentException("Item with ID '" + item.getID() + "' is already registered!");
        }

        ITEMS_BY_ID.put(item.getID(), item);
    }

    /**
     * Adds custom item to the ITEMS_BY_ID map.
     * @param item item being added to the map.
     */
    public void overrideCustomItem(CustomItem item) {
        ITEMS_BY_ID.put(item.getID(), item);
    }

    /**
     * Gets CustomItem by given id string.
     * @param id item id string.
     * @return returns the CustomItem type of item id.
     */
    public CustomItem getByID(String id) {
        return ITEMS_BY_ID.get(id);
    }

    /**
     * Gets all item ids in the map.
     * @return Set of string ids of all items.
     */
    public Set<String> getAllItemIDs() {
        return ITEMS_BY_ID.keySet(); // already uppercased
    }

    /**
     * Gets a collection of all items.
     * @return Collection of all CustomItem types.
     */
    public Collection<CustomItem> getAllItems() {
        return ITEMS_BY_ID.values();
    }

    /**
     * Checks if a given item id is already registered to the map.
     * @param id ID of item being checked.
     * @return True if registered. False if not.
     */
    public boolean isRegistered(String id) {
        return ITEMS_BY_ID.containsKey(id);
    }

    /**
     * Checks if a given item id is already registered to the map.
     * @param item Existing custom item type.
     * @return True if registered. False if not.
     */
    public boolean isRegistered(CustomItem item) {
        return ITEMS_BY_ID.containsKey(item.getID());
    }

    public void removeCustomItem(String id) {
        ITEMS_BY_ID.remove(id);
    }
}
