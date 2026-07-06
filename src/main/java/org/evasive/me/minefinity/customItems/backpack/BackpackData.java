package org.evasive.me.minefinity.customItems.backpack;

import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Per-player backpack contents, owned by the customItems module and persisted centrally by
 * playerdata as the {@code backpack_storage} component column.
 */
public class BackpackData implements PlayerDataComponent {

    private Map<String, Integer> backpackStorage = new HashMap<>();

    public Map<String, Integer> getBackpackStorage() {
        return Collections.unmodifiableMap(backpackStorage);
    }

    public Integer getBackpackStorageValue(String itemId) {
        return backpackStorage.get(itemId);
    }

    public void setBackpackStorage(String itemId, Integer value) {
        backpackStorage.put(itemId, value);
    }

    public void changeBackpackStorage(String itemId, Integer value) {
        backpackStorage.put(itemId, backpackStorage.getOrDefault(itemId, 0) + value);
    }

    public void clearBackpackStorage() {
        backpackStorage.clear();
    }

    public void setBackpackStorage(Map<String, Integer> backpackStorage) {
        this.backpackStorage = backpackStorage;
    }
}
