package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.mining.abilities.criticalfracture.CriticalFracture;
import org.evasive.me.minefinity.mining.abilities.criticalfracture.data.CriticalMap;
import org.evasive.me.minefinity.mining.abilities.earlybird.EarlyBird;
import org.evasive.me.minefinity.mining.abilities.structuredforce.StructuredForce;
import org.evasive.me.minefinity.mining.abilities.structuredforce.data.StreakMap;
import org.evasive.me.minefinity.mining.utils.AnimationIDs;

import java.util.HashMap;
import java.util.Map;

public class MiningAbilityRegistry {

    private final AnimationIDs animationIDs;
    private final Map<String, MiningAbility> abilities;

    public MiningAbilityRegistry(AnimationIDs animationIDs) {
        this.animationIDs = animationIDs;

        this.abilities = new HashMap<>();
        initializeAbilities();
    }

    private void initializeAbilities(){
        abilities.put(PickaxeAbilities.CRITICAL_FRACTURE.name(), new CriticalFracture(new CriticalMap()));
        abilities.put(PickaxeAbilities.STRUCTURED_FORCE.name(), new StructuredForce(new StreakMap()));
        abilities.put(PickaxeAbilities.EARLY_BIRD.name(), new EarlyBird());
    }

    public MiningAbility getAbility(String abilityId){
        return abilities.get(abilityId);
    }

}
