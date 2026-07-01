package org.evasive.me.minefinity.towns.structures.forge.blacksmith.data;

import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A player's blacksmith forge queue (max 5 slots). Wraps the forge-item map so it can be a
 * registered {@link PlayerDataComponent} owned by the forge feature.
 */
public class ForgeItems implements PlayerDataComponent {

    private final Map<Integer, BaseForgeItem> items = new HashMap<>();

    public Map<Integer, BaseForgeItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public BaseForgeItem getItem(int slot) {
        return items.get(slot);
    }

    public void setItem(int slot, BaseForgeItem baseForgeItem) {
        if (slot < 1 || slot > 5)
            throw new IllegalArgumentException("Forge slot out of bounds");
        items.put(slot, baseForgeItem);
    }

    public boolean removeItem(int slot) {
        return items.remove(slot) != null;
    }

    public boolean isSlotEmpty(int slot) {
        return !items.containsKey(slot);
    }
}
