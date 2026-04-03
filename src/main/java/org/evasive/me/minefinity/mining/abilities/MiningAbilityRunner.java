package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.customItems.itembuilder.data.PickaxeData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.resolvers.PickaxeResolver;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;

import java.util.ArrayList;
import java.util.List;

public class MiningAbilityRunner {

    PickaxeResolver pickaxeResolver;
    MiningAbilityRegistry miningAbilityRegistry;

    public MiningAbilityRunner(MiningAbilityRegistry miningAbilityRegistry, PickaxeResolver pickaxeResolver) {
        this.miningAbilityRegistry = miningAbilityRegistry;
        this.pickaxeResolver = pickaxeResolver;
    }

    public void runOnHit(BasePickaxeItem basePickaxeItem, HitContext hitContext){
        List<PickaxeAbilities> pickaxeAbilitiesList = new ArrayList<>();
        PickaxeData pickaxeData = pickaxeResolver.resolve(basePickaxeItem);
        for (BasePickaxeComponent part : pickaxeData.getPickaxeParts()) {
            if(part == null) continue;
            for (String abilityId : part.getPickaxeAbilityList()) {
                MiningAbility miningAbility = miningAbilityRegistry.getAbility(abilityId);
                PickaxeAbilities pickaxeAbility = PickaxeAbilities.valueOf(abilityId);
                if(miningAbility == null || pickaxeAbilitiesList.contains(pickaxeAbility)) continue;
                pickaxeAbilitiesList.add(pickaxeAbility);
                miningAbilityRegistry.getAbility(abilityId).onHit(hitContext);
            }
        }
    }

    public void runOnBreak(BasePickaxeItem basePickaxeItem, BreakContext breakContext){

        List<PickaxeAbilities> pickaxeAbilitiesList = new ArrayList<>();

        PickaxeData pickaxeData = pickaxeResolver.resolve(basePickaxeItem);
        for (BasePickaxeComponent part : pickaxeData.getPickaxeParts()) {
            if(part == null) continue;
            for (String abilityId : part.getPickaxeAbilityList()) {
                MiningAbility miningAbility = miningAbilityRegistry.getAbility(abilityId);
                PickaxeAbilities pickaxeAbility = PickaxeAbilities.valueOf(abilityId);
                if(miningAbility == null || pickaxeAbilitiesList.contains(pickaxeAbility)) continue;
                pickaxeAbilitiesList.add(pickaxeAbility);
                miningAbilityRegistry.getAbility(abilityId).onBreak(breakContext);
            }
        }
    }

}
