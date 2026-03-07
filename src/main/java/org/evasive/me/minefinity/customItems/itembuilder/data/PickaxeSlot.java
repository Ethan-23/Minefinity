package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.NamespacedKey;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public enum PickaxeSlot {
    HEAD(PICKAXE_HEAD_KEY, CustomItemType.PICKAXE_HEAD),
    CORE(PICKAXE_CORE_KEY, CustomItemType.PICKAXE_CORE),
    HANDLE(PICKAXE_HANDLE_KEY, CustomItemType.PICKAXE_HANDLE);

    private final NamespacedKey key;
    private final CustomItemType expectedType;

    PickaxeSlot(NamespacedKey key, CustomItemType expectedType) {
        this.key = key;
        this.expectedType = expectedType;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public CustomItemType getExpectedType() {
        return expectedType;
    }

    public static PickaxeSlot fromString(String input) {
        for (PickaxeSlot slot : values()) {
            if (slot.name().equalsIgnoreCase(input)) {
                return slot;
            }
        }
        return null;
    }
}
