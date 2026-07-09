package org.evasive.me.minefinity.customItems.itembuilder.data.parts;

public enum PartSlots {
    HEAD,
    CORE,
    HANDLE
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
