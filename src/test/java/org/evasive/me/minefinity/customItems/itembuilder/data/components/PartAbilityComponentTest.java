package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartAbilityComponentTest {

    @Test
    void toggleAddsAnAbilityThenRemovesItOnTheSecondCall() {
        PartAbilityComponent component = new PartAbilityComponent();

        component.toggle("CRITICAL_FRACTURE");
        assertTrue(component.getValue().contains("CRITICAL_FRACTURE"));

        component.toggle("CRITICAL_FRACTURE");
        assertFalse(component.getValue().contains("CRITICAL_FRACTURE"), "a second toggle removes it");
    }

    @Test
    void togglingDistinctAbilitiesAccumulatesThem() {
        PartAbilityComponent component = new PartAbilityComponent();
        component.toggle("CRITICAL_FRACTURE");
        component.toggle("METAL_DETECT");

        assertEquals(2, component.getValue().size());
        assertTrue(component.getValue().contains("METAL_DETECT"));
    }

    @Test
    void abilitiesRoundTripThroughConfig() {
        PartAbilityComponent original = new PartAbilityComponent();
        original.toggle("CRITICAL_FRACTURE");
        original.toggle("STRUCTURED_FORCE");

        YamlConfiguration s = new YamlConfiguration();
        original.saveToConfig(s);

        PartAbilityComponent loaded = new PartAbilityComponent();
        loaded.loadFromConfig(s);

        assertEquals(original.getValue(), loaded.getValue());
    }

    @Test
    void anEmptyAbilityListWritesNothing() {
        PartAbilityComponent empty = new PartAbilityComponent();
        YamlConfiguration s = new YamlConfiguration();

        empty.saveToConfig(s);

        assertFalse(s.contains("pickaxe-abilities"));
    }
}
