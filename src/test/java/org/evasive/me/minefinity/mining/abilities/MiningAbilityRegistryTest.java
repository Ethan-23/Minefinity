package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.mining.abilities.criticalfracture.CriticalFracture;
import org.evasive.me.minefinity.mining.abilities.earlybird.EarlyBird;
import org.evasive.me.minefinity.mining.abilities.metaldetector.MetalDetector;
import org.evasive.me.minefinity.mining.abilities.structuredforce.StructuredForce;
import org.evasive.me.minefinity.mining.utils.AnimationIDs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

class MiningAbilityRegistryTest {

    private MiningAbilityRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new MiningAbilityRegistry(mock(AbilityNotifier.class));
    }

    @Test
    void eachRegisteredAbilityResolvesToItsConcreteType() {
        assertInstanceOf(CriticalFracture.class, registry.getAbility(PickaxeAbilities.CRITICAL_FRACTURE.name()));
        assertInstanceOf(StructuredForce.class, registry.getAbility(PickaxeAbilities.STRUCTURED_FORCE.name()));
        assertInstanceOf(EarlyBird.class, registry.getAbility(PickaxeAbilities.EARLY_BIRD.name()));
        assertInstanceOf(MetalDetector.class, registry.getAbility(PickaxeAbilities.METAL_DETECT.name()));
    }

    @Test
    void unknownAbilityIdReturnsNull() {
        assertNull(registry.getAbility("NOT_AN_ABILITY"));
    }

    @Test
    void nullAbilityIdReturnsNullRatherThanThrowing() {
        assertNull(registry.getAbility(null));
    }

    @Test
    void abilityIdIsCaseSensitive() {
        assertNull(registry.getAbility("early_bird"),
                "lookup is keyed on the exact enum name; lower-case must not resolve");
    }

    @Test
    void declaredAbilitiesWithoutAnImplementationAreNotRegistered() {
        // Several PickaxeAbilities values exist in the enum but have no runtime implementation wired up.
        assertNull(registry.getAbility(PickaxeAbilities.MOLDABLE_GRIP.name()));
        assertNull(registry.getAbility(PickaxeAbilities.SEISMIC_TAP.name()));
        assertNull(registry.getAbility(PickaxeAbilities.FRACTURED_VEIN.name()));
        assertNull(registry.getAbility(PickaxeAbilities.ITCHY.name()));
    }

    @Test
    void lookupIsStableAcrossCallsButReturnsTheSharedInstance() {
        MiningAbility first = registry.getAbility(PickaxeAbilities.EARLY_BIRD.name());
        MiningAbility second = registry.getAbility(PickaxeAbilities.EARLY_BIRD.name());
        assertSame(first, second, "the registry hands out the same cached ability instance");
    }
}
