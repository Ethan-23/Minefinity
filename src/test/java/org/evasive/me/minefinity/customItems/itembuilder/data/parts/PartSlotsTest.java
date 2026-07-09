package org.evasive.me.minefinity.customItems.itembuilder.data.parts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartSlotsTest {

    @Test
    void fromStringResolvesExactNames() {
        assertEquals(PartSlots.HEAD, PartSlots.fromString("HEAD"));
        assertEquals(PartSlots.CORE, PartSlots.fromString("CORE"));
        assertEquals(PartSlots.HANDLE, PartSlots.fromString("HANDLE"));
    }

    @Test
    void fromStringIsCaseInsensitive() {
        assertEquals(PartSlots.HEAD, PartSlots.fromString("head"));
        assertEquals(PartSlots.HEAD, PartSlots.fromString("Head"));
        assertEquals(PartSlots.HANDLE, PartSlots.fromString("hAnDlE"));
    }

    @Test
    void legacyToolPrefixedNamesNoLongerResolve() {
        // The pre-refactor flattened names (PICKAXE_HEAD, AXE_HANDLE, ...) were removed. They MUST map
        // to null so that old PDC/config keys are silently dropped instead of resurrected — this is the
        // exact guarantee ToolPartComponent's crash fix relies on.
        assertNull(PartSlots.fromString("PICKAXE_HEAD"));
        assertNull(PartSlots.fromString("PICKAXE_CORE"));
        assertNull(PartSlots.fromString("AXE_HANDLE"));
    }

    @Test
    void fromStringReturnsNullForUnknownEmptyOrNull() {
        assertNull(PartSlots.fromString("BANANA"));
        assertNull(PartSlots.fromString(""));
        assertNull(PartSlots.fromString(null), "null must not throw — equalsIgnoreCase(null) is false");
    }
}
