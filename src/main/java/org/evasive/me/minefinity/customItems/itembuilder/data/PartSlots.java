package org.evasive.me.minefinity.customItems.itembuilder.data;

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
}
