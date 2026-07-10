package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComponentConfigRoundTripTest {

    private YamlConfiguration written(ItemComponent component) {
        YamlConfiguration section = new YamlConfiguration();
        component.saveToConfig(section);
        return section;
    }

    @Test
    void flavorTextRoundTrips() {
        FlavorTextComponent original = new FlavorTextComponent();
        original.setValue("A shiny rock");

        FlavorTextComponent loaded = new FlavorTextComponent();
        loaded.loadFromConfig(written(original));

        assertEquals("A shiny rock", loaded.getValue());
    }

    @Test
    void sellValueRoundTrips() {
        ValueComponent original = new ValueComponent();
        original.setValue(12.5f);

        ValueComponent loaded = new ValueComponent();
        loaded.loadFromConfig(written(original));

        assertEquals(12.5f, loaded.getValue(), 1e-6);
    }

    @Test
    void glowRoundTripsBothTrueAndFalse() {
        GlowComponent on = new GlowComponent();
        on.setValue(true);
        GlowComponent onLoaded = new GlowComponent();
        onLoaded.loadFromConfig(written(on));
        assertTrue(onLoaded.getValue());

        GlowComponent off = new GlowComponent();
        off.setValue(false);
        GlowComponent offLoaded = new GlowComponent();
        offLoaded.loadFromConfig(written(off));
        assertFalse(offLoaded.getValue());
    }

    @Test
    void statsRoundTrip() {
        StatsComponent original = new StatsComponent();
        original.setStat(Stats.MINING_SPEED, 30);
        original.setStat(Stats.BREAKING_POWER, 2);

        StatsComponent loaded = new StatsComponent();
        loaded.loadFromConfig(written(original));

        assertEquals(30, loaded.getValue().get("MINING_SPEED"));
        assertEquals(2, loaded.getValue().get("BREAKING_POWER"));
    }

    @Test
    void equipmentSlotsRoundTripAndUnknownSlotsAreDropped() {
        EquipmentSlotComponent original = new EquipmentSlotComponent();
        original.setValue(Set.of(EquipmentSlot.HAND, EquipmentSlot.HEAD));

        YamlConfiguration s = written(original);
        List<String> withJunk = new ArrayList<>(s.getStringList("equipment-slot"));
        withJunk.add("NOT_A_SLOT");
        s.set("equipment-slot", withJunk);

        EquipmentSlotComponent loaded = new EquipmentSlotComponent();
        loaded.loadFromConfig(s);

        assertEquals(Set.of(EquipmentSlot.HAND, EquipmentSlot.HEAD), loaded.getValue(),
                "the bogus slot name is skipped, valid ones survive");
    }

    @Test
    void acceptableToolsRoundTripAndUnknownTypesAreDropped() {
        AcceptableToolsComponent original = new AcceptableToolsComponent();
        original.setValue(Set.of(CustomItemType.PICKAXE, CustomItemType.AXE));

        YamlConfiguration s = written(original);
        List<String> withJunk = new ArrayList<>(s.getStringList("acceptable-tools"));
        withJunk.add("NOT_A_TYPE");
        s.set("acceptable-tools", withJunk);

        AcceptableToolsComponent loaded = new AcceptableToolsComponent();
        loaded.loadFromConfig(s);

        assertEquals(Set.of(CustomItemType.PICKAXE, CustomItemType.AXE), loaded.getValue());
    }

    @Test
    void partSlotCategoriesRoundTripAndLegacyNamesAreDropped() {
        PartSlotComponent original = new PartSlotComponent();
        original.setValue(Set.of(PartSlots.HEAD));

        YamlConfiguration s = written(original);
        List<String> withLegacy = new ArrayList<>(s.getStringList("part-slot"));
        withLegacy.add("PICKAXE_HEAD");
        s.set("part-slot", withLegacy);

        PartSlotComponent loaded = new PartSlotComponent();
        loaded.loadFromConfig(s);

        assertEquals(Set.of(PartSlots.HEAD), loaded.getValue(), "the legacy PICKAXE_HEAD is dropped");
    }

    @Test
    void storageListRoundTripsPreservingOrder() {
        StorageListComponent original = new StorageListComponent();
        original.setValue(new ArrayList<>(List.of("TUFF", "COAL")));

        StorageListComponent loaded = new StorageListComponent();
        loaded.loadFromConfig(written(original));

        assertEquals(List.of("TUFF", "COAL"), loaded.getValue());
    }

    @Test
    void fuelAndStorageAmountsRoundTrip() {
        FuelAmountComponent fuel = new FuelAmountComponent();
        fuel.setValue(3);
        FuelAmountComponent fuelLoaded = new FuelAmountComponent();
        fuelLoaded.loadFromConfig(written(fuel));
        assertEquals(3, fuelLoaded.getValue());

        StorageAmountComponent storage = new StorageAmountComponent();
        storage.setValue(640);
        StorageAmountComponent storageLoaded = new StorageAmountComponent();
        storageLoaded.loadFromConfig(written(storage));
        assertEquals(640, storageLoaded.getValue());
    }

    @Test
    void visualMaterialRoundTrips() {
        VisualMaterialComponent original = new VisualMaterialComponent();
        original.setValue(Material.BUNDLE);

        VisualMaterialComponent loaded = new VisualMaterialComponent();
        loaded.loadFromConfig(written(original));

        assertEquals(Material.BUNDLE, loaded.getValue());
    }

    // ---- Behaviour these DOCUMENT (candidate bugs; see the accompanying write-up) ----

    @Test
    void stackSizeConfigLoadDoesNotClampUnlikeTheEditor() {
        // The GUI editor clamps to 1..99, but loadFromConfig performs no range check, so an out-of-range
        // config value flows straight into buildItem()/setMaxStackSize. Pinned here so the gap is visible.
        StackSizeComponent loaded = new StackSizeComponent();
        YamlConfiguration s = new YamlConfiguration();
        s.set("stack-size", 200);

        loaded.loadFromConfig(s);

        assertEquals(200, loaded.getValue(), "config load performs no range validation");
    }

    @Test
    void sellValueConfigLoadAcceptsNegativesUnlikeTheEditor() {
        // promptFloat rejects negatives in the GUI; loadFromConfig/setValue do not.
        ValueComponent loaded = new ValueComponent();
        YamlConfiguration s = new YamlConfiguration();
        s.set("sell-value", -5.0);

        loaded.loadFromConfig(s);

        assertEquals(-5.0f, loaded.getValue(), 1e-6, "config load performs no sign validation");
    }
}
