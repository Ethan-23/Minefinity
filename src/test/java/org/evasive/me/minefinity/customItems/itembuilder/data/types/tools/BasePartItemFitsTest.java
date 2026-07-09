package org.evasive.me.minefinity.customItems.itembuilder.data.types.tools;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.CustomItemType;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BasePartItemFitsTest {

    private BasePartItem part(Set<PartSlots> categories, Set<CustomItemType> acceptableTools) {
        BasePartItem part = new BasePartItem("TEST_PART", Material.STICK, "Test Part", Rarity.MINOR);
        part.slotComponent().setValue(categories);
        part.acceptableToolsComponent().setValue(acceptableTools);
        return part;
    }

    @Test
    void fitsWhenCategoryMatchesAndToolIsAccepted() {
        BasePartItem head = part(Set.of(PartSlots.HEAD), Set.of(CustomItemType.PICKAXE));
        assertTrue(head.fits(PartSlots.HEAD, CustomItemType.PICKAXE));
    }

    @Test
    void doesNotFitWhenCategoryDiffers() {
        BasePartItem head = part(Set.of(PartSlots.HEAD), Set.of(CustomItemType.PICKAXE));
        assertFalse(head.fits(PartSlots.CORE, CustomItemType.PICKAXE));
    }

    @Test
    void doesNotFitWhenToolTypeNotAccepted() {
        BasePartItem pickaxeOnly = part(Set.of(PartSlots.HEAD), Set.of(CustomItemType.PICKAXE));
        assertFalse(pickaxeOnly.fits(PartSlots.HEAD, CustomItemType.AXE));
    }

    @Test
    void emptyAcceptableToolsMeansEveryTool() {
        // The documented default: a part that declares no acceptable tools is universal.
        BasePartItem universal = part(Set.of(PartSlots.HEAD), Set.of());
        assertTrue(universal.fits(PartSlots.HEAD, CustomItemType.PICKAXE));
        assertTrue(universal.fits(PartSlots.HEAD, CustomItemType.AXE));
    }

    @Test
    void categoryStillGatesEvenWhenAllToolsAreAccepted() {
        BasePartItem universalHead = part(Set.of(PartSlots.HEAD), Set.of());
        assertFalse(universalHead.fits(PartSlots.CORE, CustomItemType.PICKAXE),
                "universal-tool does not mean universal-slot");
    }

    @Test
    void aPartMayDeclareMultipleCategories() {
        BasePartItem flexible = part(Set.of(PartSlots.HEAD, PartSlots.CORE), Set.of(CustomItemType.PICKAXE));
        assertTrue(flexible.fits(PartSlots.HEAD, CustomItemType.PICKAXE));
        assertTrue(flexible.fits(PartSlots.CORE, CustomItemType.PICKAXE));
        assertFalse(flexible.fits(PartSlots.HANDLE, CustomItemType.PICKAXE));
    }
}
