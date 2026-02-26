package org.evasive.me.minefinity.customItems.backpack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BackpackStorage {

    private final Map<String, Integer> backpackStorage;

    public BackpackStorage() {
        this.backpackStorage = new HashMap<>();
    }

    public BackpackStorage(Map<String, Integer> backpackStorage) {
        this.backpackStorage = backpackStorage;
    }

    public int getResourceCount(String resourceId) {
        return backpackStorage.getOrDefault(resourceId, 0);
    }

    public void setResourceCount(String resourceId, int count) {
        backpackStorage.put(resourceId, count);
    }

    public Map<String, Integer> getBackpackMap() {
        return Collections.unmodifiableMap(backpackStorage);
    }

    public void clearBackpack() {
        this.backpackStorage.clear();
    }
}
