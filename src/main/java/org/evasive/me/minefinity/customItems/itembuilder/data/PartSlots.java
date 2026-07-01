package org.evasive.me.minefinity.customItems.itembuilder.data;

import java.util.Set;

public enum PartSlots {
    HEAD,
    CORE,
    HANDLE,
    PICKAXE_HEAD,
    PICKAXE_CORE,
    PICKAXE_HANDLE,
    AXE_HEAD,
    AXE_HANDLE
    ;

    public static PartSlots fromString(String name) {
        for (PartSlots slot : values()) {
            if (slot.name().equalsIgnoreCase(name)) {
                return slot;
            }
        }

        return null;
    }

    public PartSlots category() {
        return switch (this) {
            case HEAD, PICKAXE_HEAD, AXE_HEAD -> HEAD;
            case CORE, PICKAXE_CORE -> CORE;
            case HANDLE, PICKAXE_HANDLE, AXE_HANDLE -> HANDLE;
        };
    }

    public static boolean isCompatible(Set<PartSlots> partSlots, PartSlots toolSlot) {
        if (partSlots == null) return false;
        return partSlots.contains(toolSlot) || partSlots.contains(toolSlot.category());
    }
}
