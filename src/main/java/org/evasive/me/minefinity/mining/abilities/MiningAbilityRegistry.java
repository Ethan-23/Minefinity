package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.mining.abilities.criticalfracture.CriticalFracture;
import org.evasive.me.minefinity.mining.abilities.criticalfracture.data.CriticalMap;
import org.evasive.me.minefinity.mining.abilities.earlybird.EarlyBird;
import org.evasive.me.minefinity.mining.abilities.metaldetector.MetalDetector;
import org.evasive.me.minefinity.mining.abilities.structuredforce.StructuredForce;
import org.evasive.me.minefinity.mining.abilities.structuredforce.data.StreakMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningAbilityRegistry {

    private final Map<String, MiningAbility> abilities;
    private final StreakMap streakMap;
    private final CriticalMap criticalMap;
    private final AbilityNotifier abilityNotifier;

    public MiningAbilityRegistry(AbilityNotifier abilityNotifier) {
        this.abilityNotifier = abilityNotifier;
        this.criticalMap = new CriticalMap();
        this.streakMap = new StreakMap();
        this.abilities = new HashMap<>();
        initializeAbilities();
    }

    private void initializeAbilities(){
        abilities.put(PickaxeAbilities.CRITICAL_FRACTURE.name(), new CriticalFracture(criticalMap));
        abilities.put(PickaxeAbilities.STRUCTURED_FORCE.name(), new StructuredForce(streakMap, abilityNotifier));
        abilities.put(PickaxeAbilities.EARLY_BIRD.name(), new EarlyBird());
        abilities.put(PickaxeAbilities.METAL_DETECT.name(), new MetalDetector(abilityNotifier));
    }

    public MiningAbility getAbility(String abilityId){
        return abilities.get(abilityId);
    }

    public void removePlayerData(UUID uuid){
        criticalMap.removeCritical(uuid);
        streakMap.removeStreak(uuid);
    }

}
