package org.evasive.me.minevolutionCore.customItems.pickaxe;

import org.bukkit.NamespacedKey;
import org.evasive.me.minevolutionCore.customItems.items.CustomItemType;

import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.*;

public enum PickaxeSlot {
    HEAD("head", headKey, CustomItemType.PICKAXE_HEAD),
    CORE("core", coreKey, CustomItemType.PICKAXE_CORE),
    HANDLE("handle", handleKey, CustomItemType.PICKAXE_HANDLE);

    private final String name;
    private final NamespacedKey key;
    private final CustomItemType expectedType;

    PickaxeSlot(String name, NamespacedKey key, CustomItemType expectedType) {
        this.name = name;
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
            if (slot.name.equalsIgnoreCase(input)) {
                return slot;
            }
        }
        return null;
    }
}
