package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.customItems.itembuilder.data.ToolItemData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
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
        ToolItemData pickaxeData = pickaxeResolver.resolve(basePickaxeItem);
        for (BasePartItem part : pickaxeData.getPickaxeParts()) {
            if(part == null) continue;
            for (String abilityId : part.getAbilityList()) {
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

        ToolItemData pickaxeData = pickaxeResolver.resolve(basePickaxeItem);
        for (BasePartItem part : pickaxeData.getPickaxeParts()) {
            if(part == null) continue;
            for (String abilityId : part.getAbilityList()) {
                MiningAbility miningAbility = miningAbilityRegistry.getAbility(abilityId);
                PickaxeAbilities pickaxeAbility = PickaxeAbilities.valueOf(abilityId);
                if(miningAbility == null || pickaxeAbilitiesList.contains(pickaxeAbility)) continue;
                pickaxeAbilitiesList.add(pickaxeAbility);
                miningAbilityRegistry.getAbility(abilityId).onBreak(breakContext);
            }
        }
    }

}
