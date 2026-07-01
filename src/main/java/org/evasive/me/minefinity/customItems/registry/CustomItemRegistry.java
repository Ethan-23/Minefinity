package org.evasive.me.minefinity.customItems.registry;

import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomItemRegistry {

    private final Map<String, BaseCustomItem> ITEMS_BY_ID = new HashMap<>();

    public CustomItemRegistry() {
        for (BaseCustomItem item : getAllItems()) {
            item.getBaseItem().buildItem();
        }
    }

    /**
     * Adds new custom item to the ITEMS_BY_ID map.
     * @param item item being added to the map.
     */
    public void registerCustomItem(BaseCustomItem item) {
        if (ITEMS_BY_ID.containsKey(item.getID())) {
            throw new IllegalArgumentException("Item with ID '" + item.getID() + "' is already registered!");
        }

        ITEMS_BY_ID.put(item.getID(), item);
    }

    /**
     * Adds custom item to the ITEMS_BY_ID map.
     * @param baseCustomItem item being added to the map.
     */
    public void overrideCustomItem(BaseCustomItem baseCustomItem) {
        ITEMS_BY_ID.put(baseCustomItem.getID(), baseCustomItem);
    }

    /**
     * Gets CustomItem by given id string.
     * @param id item id string.
     * @return returns the CustomItem type of item id.
     */
    public BaseCustomItem getByID(String id) {
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
    public Collection<BaseCustomItem> getAllItems() {
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
    public boolean isRegistered(BaseCustomItem item) {
        return ITEMS_BY_ID.containsKey(item.getID());
    }

    public void removeCustomItem(String id) {
        ITEMS_BY_ID.remove(id);
    }
}
