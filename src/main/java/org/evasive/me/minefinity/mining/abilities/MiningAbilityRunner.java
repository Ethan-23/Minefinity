package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.customItems.itembuilder.data.types.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.tools.BasePickaxeItem;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MiningAbilityRunner {

    MiningAbilityRegistry miningAbilityRegistry;

    public MiningAbilityRunner(MiningAbilityRegistry miningAbilityRegistry) {
        this.miningAbilityRegistry = miningAbilityRegistry;
    }

    public void runApplyStats(BasePickaxeItem basePickaxeItem, HitContext hitContext){
        forEachAbility(basePickaxeItem, ability -> ability.applyStats(hitContext));
    }

    public void runOnHit(BasePickaxeItem basePickaxeItem, HitContext hitContext){
        forEachAbility(basePickaxeItem, ability -> ability.onHit(hitContext));
    }

    public void runOnBreak(BasePickaxeItem basePickaxeItem, BreakContext breakContext){
        forEachAbility(basePickaxeItem, ability -> ability.onBreak(breakContext));
    }

    /** Walks every distinct ability installed on the pickaxe (de-duplicated across parts) and applies the
     *  given action. Shared by the apply-stats / hit / break passes so they can never drift apart. */
    private void forEachAbility(BasePickaxeItem basePickaxeItem, Consumer<MiningAbility> action){
        List<PickaxeAbilities> firedAbilities = new ArrayList<>();
        for (BasePartItem part : basePickaxeItem.getInstalledParts()) {
            if(part == null) continue;
            for (String abilityId : part.abilityComponent().getValue()) {
                MiningAbility miningAbility = miningAbilityRegistry.getAbility(abilityId);
                PickaxeAbilities pickaxeAbility = PickaxeAbilities.getPickaxeAbilities(abilityId);
                if(miningAbility == null || firedAbilities.contains(pickaxeAbility)) continue;
                firedAbilities.add(pickaxeAbility);
                action.accept(miningAbility);
            }
        }
    }

}
