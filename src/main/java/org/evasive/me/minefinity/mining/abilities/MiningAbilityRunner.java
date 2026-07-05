package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.customItems.itembuilder.data.ToolItemData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;

import java.util.ArrayList;
import java.util.List;

public class MiningAbilityRunner {

    MiningAbilityRegistry miningAbilityRegistry;

    public MiningAbilityRunner(MiningAbilityRegistry miningAbilityRegistry) {
        this.miningAbilityRegistry = miningAbilityRegistry;
    }

    public void runOnHit(BasePickaxeItem basePickaxeItem, HitContext hitContext){
        List<PickaxeAbilities> pickaxeAbilitiesList = new ArrayList<>();
        ToolItemData pickaxeData = new ToolItemData(basePickaxeItem);
        for (BasePartItem part : pickaxeData.getPickaxeParts()) {
            if(part == null) continue;
            for (String abilityId : part.abilityComponent().getValue()) {
                MiningAbility miningAbility = miningAbilityRegistry.getAbility(abilityId);

                PickaxeAbilities pickaxeAbility = PickaxeAbilities.getPickaxeAbilities(abilityId);

                if(miningAbility == null || pickaxeAbilitiesList.contains(pickaxeAbility)) continue;
                pickaxeAbilitiesList.add(pickaxeAbility);
                miningAbilityRegistry.getAbility(abilityId).onHit(hitContext);
            }
        }
    }

    public void runOnBreak(BasePickaxeItem basePickaxeItem, BreakContext breakContext){

        List<PickaxeAbilities> pickaxeAbilitiesList = new ArrayList<>();

        ToolItemData pickaxeData = new ToolItemData(basePickaxeItem);
        for (BasePartItem part : pickaxeData.getPickaxeParts()) {
            if(part == null) continue;
            for (String abilityId : part.abilityComponent().getValue()) {
                MiningAbility miningAbility = miningAbilityRegistry.getAbility(abilityId);
                PickaxeAbilities pickaxeAbility = PickaxeAbilities.getPickaxeAbilities(abilityId);
                if(miningAbility == null || pickaxeAbilitiesList.contains(pickaxeAbility)) continue;
                pickaxeAbilitiesList.add(pickaxeAbility);
                miningAbilityRegistry.getAbility(abilityId).onBreak(breakContext);
            }
        }
    }

}
