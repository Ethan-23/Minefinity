package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.file.YamlConfiguration;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolPartComponentConfigTest {

    @Test
    void partsRoundTripThroughConfig() {
        ToolPartComponent original = new ToolPartComponent();
        original.setPart(PartSlots.HEAD, "FLINT_HEAD");
        original.setPart(PartSlots.CORE, "ANDESITE_CORE");

        YamlConfiguration section = new YamlConfiguration();
        original.saveToConfig(section);

        ToolPartComponent loaded = new ToolPartComponent();
        loaded.loadFromConfig(section);

        assertEquals("FLINT_HEAD", loaded.getPart(PartSlots.HEAD));
        assertEquals("ANDESITE_CORE", loaded.getPart(PartSlots.CORE));
        assertNull(loaded.getPart(PartSlots.HANDLE));
    }

    @Test
    void legacyToolPrefixedKeysAreDroppedNotLoaded() {
        // A pre-refactor config used PICKAXE_HEAD-style keys. loadFromConfig must skip them (fromString
        // returns null) rather than resurrect them — the config twin of the PDC crash we fixed.
        YamlConfiguration section = new YamlConfiguration();
        section.set("parts.PICKAXE_HEAD", "FLINT_HEAD");
        section.set("parts.HEAD", "STONE_HEAD");

        ToolPartComponent loaded = new ToolPartComponent();
        loaded.loadFromConfig(section);

        assertEquals("STONE_HEAD", loaded.getPart(PartSlots.HEAD), "the valid category key is kept");
        assertEquals(1, loaded.getValue().size(), "the legacy PICKAXE_HEAD key is dropped, not kept as a bad slot");
    }

    @Test
    void anEmptyPartMapWritesNoSection() {
        ToolPartComponent empty = new ToolPartComponent();
        YamlConfiguration section = new YamlConfiguration();

        empty.saveToConfig(section);

        assertFalse(section.contains("parts"), "no parts section is written when there are no parts");
    }

    @Test
    void setPartWithBlankIdClearsTheSlot() {
        ToolPartComponent component = new ToolPartComponent();
        component.setPart(PartSlots.HEAD, "FLINT_HEAD");
        component.setPart(PartSlots.HEAD, "");

        assertNull(component.getPart(PartSlots.HEAD), "empty id removes the slot entry");
        assertTrue(component.getValue().isEmpty());
    }
}
