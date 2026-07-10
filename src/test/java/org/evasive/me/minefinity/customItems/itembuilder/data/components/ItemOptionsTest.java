package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemOptionsTest {

    @Test
    void componentBackedOptionsReportAsComponents() {
        assertTrue(ItemOptions.ACCEPTABLE_TOOLS.isComponent());
        assertTrue(ItemOptions.STATS.isComponent());
        assertFalse(ItemOptions.MATERIAL.isComponent());
        assertFalse(ItemOptions.MINEFINITY_ID.isComponent());
    }

    @Test
    void asComponentClassMatchesTheBackingComponent() {
        assertEquals(AcceptableToolsComponent.class, ItemOptions.ACCEPTABLE_TOOLS.asComponentClass());
    }

    @Test
    void scalarOptionTypePredicates() {
        assertTrue(ItemOptions.MINEFINITY_ID.isString());
        assertTrue(ItemOptions.MATERIAL.isMaterial());
        assertTrue(ItemOptions.RARITY.isEnum());
        assertFalse(ItemOptions.MATERIAL.isString());
    }

    @Test
    void toolPartTypeWiresTheThreePartOptionsIncludingAcceptableTools() {
        List<ItemOptions> options = CustomItemType.TOOL_PART.getAllOptions();
        assertTrue(options.contains(ItemOptions.PART_SLOT));
        assertTrue(options.contains(ItemOptions.PART_ABILITY));
        assertTrue(options.contains(ItemOptions.ACCEPTABLE_TOOLS),
                "the acceptable-tools tile must be reachable in the builder for tool parts");
    }

    @Test
    void everyTypeExposesTheCommonOptions() {
        for (CustomItemType type : CustomItemType.values()) {
            List<ItemOptions> options = type.getAllOptions();
            assertTrue(options.contains(ItemOptions.RARITY), type + " should expose RARITY");
            assertTrue(options.contains(ItemOptions.STATS), type + " should expose STATS");
            assertTrue(options.contains(ItemOptions.MINEFINITY_ID), type + " should expose the ID option");
        }
    }
}
