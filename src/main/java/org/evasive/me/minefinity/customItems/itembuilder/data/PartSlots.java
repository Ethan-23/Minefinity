package org.evasive.me.minefinity.customItems.itembuilder.data;

import java.util.Set;

public enum PartSlots {
    // Generic slots — a part assigned one of these fits the matching slot on ANY tool.
    HEAD,
    CORE,
    HANDLE,
    // Tool-specific slots — a part assigned one of these only fits that tool's slot.
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

    /**
     * The generic category of this slot (HEAD, CORE or HANDLE). A specific slot like
     * {@code PICKAXE_HEAD} has the category {@code HEAD}; the generic slots are their own category.
     */
    public PartSlots category() {
        return switch (this) {
            case HEAD, PICKAXE_HEAD, AXE_HEAD -> HEAD;
            case CORE, PICKAXE_CORE -> CORE;
            case HANDLE, PICKAXE_HANDLE, AXE_HANDLE -> HANDLE;
        };
    }

    /**
     * Whether a part that can fill {@code partSlots} may go into the given tool slot.
     * A part fits if it declares that exact slot, or the generic category of that slot
     * (so a generic HEAD part fits a PICKAXE_HEAD slot, but a PICKAXE_HEAD part does not
     * fit an AXE_HEAD slot).
     */
    public static boolean isCompatible(Set<PartSlots> partSlots, PartSlots toolSlot) {
        if (partSlots == null) return false;
        return partSlots.contains(toolSlot) || partSlots.contains(toolSlot.category());
    }
}
