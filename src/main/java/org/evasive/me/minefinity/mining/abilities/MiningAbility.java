package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.DropContext;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;

public interface MiningAbility {

    default void applyStats(StatsContext context) {}

    default void onHit(HitContext context) {}

    default void onBreak(BreakContext context) {}

    default void onDropRoll(DropContext context) {}

}
