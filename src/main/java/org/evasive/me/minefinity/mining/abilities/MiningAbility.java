package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;

public interface MiningAbility {

    /** Contribute stat numbers (speed, fortune, special chance…) into the mining stats for this swing.
     *  Runs before the block's mining speed is calculated and before the break's drop roll. */
    default void applyStats(HitContext context) {}

    /** Per-swing behaviour that is not a plain stat contribution (particles, hit detection, timers). */
    default void onHit(HitContext context) {}

    /** React to a resolved block break — the special-drop outcome is already decided by this point. */
    default void onBreak(BreakContext context) {}

}
