package org.evasive.me.minefinity.forge.service;

import org.evasive.me.minefinity.forge.data.BaseForgeItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ForgeMap {

    private final Map<Integer, BaseForgeItem> forgeItemsMap;

    public ForgeMap() {
        this.forgeItemsMap = new HashMap<>();
    }

    public ForgeMap(Map<Integer, BaseForgeItem> backpackStorage) {
        this.forgeItemsMap = backpackStorage;
    }

    public BaseForgeItem getForgeItem(int forgeSlot) {
        return forgeItemsMap.getOrDefault(forgeSlot, null);
    }

    public void setForgeItem(int forgeSlot, BaseForgeItem forgeItem) {
        forgeItemsMap.put(forgeSlot, forgeItem);
    }

    public Map<Integer, BaseForgeItem> getForgeItemsMap() {
        return Collections.unmodifiableMap(forgeItemsMap);
    }

    public void clearForgeSlot(int forgeSlot) {
        forgeItemsMap.remove(forgeSlot);
    }
}
