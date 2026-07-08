package org.evasive.me.minefinity.mining.blocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBlockTiersTest {

    private PlayerBlockTiers tiers;

    @BeforeEach
    void setUp() {
        tiers = new PlayerBlockTiers();
    }

    @Test
    void unlockedTierRoundTripsForAKnownWorld() {
        tiers.setUnlockedBlockTier("nether", 4);
        assertEquals(4, tiers.getUnlockedBlockTier("nether"));
    }

    @Test
    void anUnknownWorldWithNoDefaultReturnsZero() {
        assertEquals(0, tiers.getUnlockedBlockTier("aether"));
    }

    @Test
    void anUnknownWorldFallsBackToTheDefaultWorldTier() {
        // Gotcha: an unset world does not return 0 — it borrows whatever the "world" key holds.
        tiers.setUnlockedBlockTier("world", 7);
        assertEquals(7, tiers.getUnlockedBlockTier("the_end"),
                "unknown worlds inherit the 'world' tier, so a tier unlock bleeds across dimensions");
    }

    @Test
    void queryingTheDefaultWorldItselfWhenUnsetReturnsZero() {
        assertEquals(0, tiers.getUnlockedBlockTier("world"));
    }

    @Test
    void negativeTiersAreStoredWithoutValidation() {
        tiers.setUnlockedBlockTier("world", -3);
        assertEquals(-3, tiers.getUnlockedBlockTier("world"),
                "there is no lower-bound guard on the stored tier");
    }

    @Test
    void selectedTierRoundTrips() {
        tiers.setSelectedBlockTier("world", "Coal");
        assertEquals("Coal", tiers.getSelectedBlockTier("world"));
    }

    @Test
    void gettingASelectedTierForAnUnknownWorldThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> tiers.getSelectedBlockTier("mars"));
        assertTrue(ex.getMessage().contains("mars"), "the message should name the offending world");
    }

    @Test
    void hasWorldUnlockedReflectsTheSelectedMapNotTheUnlockedMap() {
        // Naming/logic mismatch: hasWorldUnlocked() inspects the *selected* tiers, so unlocking a tier
        // does NOT flip it — only selecting one does. Pinned so that "fixing" the check is a deliberate
        // decision rather than an accident.
        tiers.setUnlockedBlockTier("world", 5);
        assertFalse(tiers.hasWorldUnlocked("world"),
                "setting only an unlocked tier leaves hasWorldUnlocked false");

        tiers.setSelectedBlockTier("world", "Coal");
        assertTrue(tiers.hasWorldUnlocked("world"),
                "selecting a tier is what actually makes hasWorldUnlocked report true");
    }

    @Test
    void overwritingATierReplacesTheStoredValue() {
        tiers.setUnlockedBlockTier("nether", 1);
        tiers.setUnlockedBlockTier("nether", 9);
        assertEquals(9, tiers.getUnlockedBlockTier("nether"));
    }
}
