package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.core.rarity.Rarity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PickaxeAbilitiesTest {

    @Test
    void lookupByExactNameReturnsTheConstant() {
        assertEquals(PickaxeAbilities.CRITICAL_FRACTURE,
                PickaxeAbilities.getPickaxeAbilities("CRITICAL_FRACTURE"));
    }

    @Test
    void lookupIsCaseSensitiveSoLowercaseFails() {
        // valueOf is case-sensitive; the lowercase form is not recognised and comes back null.
        assertNull(PickaxeAbilities.getPickaxeAbilities("critical_fracture"));
    }

    @Test
    void anUnknownIdReturnsNullRatherThanThrowing() {
        assertNull(PickaxeAbilities.getPickaxeAbilities("NOT_A_REAL_ABILITY"));
    }

    @Test
    void aNullIdSlipsPastTheIllegalArgumentCatchAndThrowsNpe() {
        // The catch only handles IllegalArgumentException, but valueOf(null) throws NullPointerException,
        // so a null id is NOT converted into the intended safe null return — it propagates to the caller.
        assertThrows(NullPointerException.class, () -> PickaxeAbilities.getPickaxeAbilities(null));
    }

    @Test
    void onlyMetalDetectCarriesASound() {
        assertTrue(PickaxeAbilities.METAL_DETECT.hasSound());
        assertEquals("block.beacon.activate", PickaxeAbilities.METAL_DETECT.getSoundKey());

        assertFalse(PickaxeAbilities.CRITICAL_FRACTURE.hasSound());
        assertNull(PickaxeAbilities.CRITICAL_FRACTURE.getSoundKey());
    }

    @Test
    void hasSoundIsAlwaysConsistentWithGetSoundKey() {
        for (PickaxeAbilities ability : PickaxeAbilities.values()) {
            assertEquals(ability.getSoundKey() != null, ability.hasSound(),
                    ability.name() + ": hasSound() must agree with whether a sound key exists");
        }
    }

    @Test
    void everyAbilityHasADescriptionAndRarity() {
        for (PickaxeAbilities ability : PickaxeAbilities.values()) {
            assertNotNull(ability.getRarity(), ability.name() + " is missing a rarity");
            assertNotNull(ability.getDescription(), ability.name() + " is missing a description");
            assertFalse(ability.getDescription().isBlank(), ability.name() + " has a blank description");
        }
    }

    @Test
    void nameDisplayHumanisesTheEnumNameAndAppliesRarityColour() {
        String display = PickaxeAbilities.CRITICAL_FRACTURE.getAbilityNameDisplay();

        assertTrue(display.startsWith("<#"), "should be prefixed with the rarity hex-colour tag");
        assertTrue(display.contains("Critical Fracture"), "underscores become spaces and words are title-cased");
        assertFalse(display.contains("CRITICAL_FRACTURE"), "the raw enum name should not leak through");
    }

    @Test
    void fullDisplayCombinesNameSeparatorAndDescription() {
        PickaxeAbilities ability = PickaxeAbilities.METAL_DETECT;
        String display = ability.getAbilityDisplay();

        assertTrue(display.contains("Metal Detect"), "the humanised name is present");
        assertTrue(display.contains("<dark_gray>"), "the name and description are joined by a coloured dash separator");
        assertTrue(display.contains(ability.getDescription()), "the description is appended verbatim");
    }

    @Test
    void raritiesAreReportedPerAbility() {
        assertEquals(Rarity.MINOR, PickaxeAbilities.MOLDABLE_GRIP.getRarity());
        assertEquals(Rarity.UNIQUE, PickaxeAbilities.EARLY_BIRD.getRarity());
        assertEquals(Rarity.RADIANT, PickaxeAbilities.ITCHY.getRarity());
    }
}
