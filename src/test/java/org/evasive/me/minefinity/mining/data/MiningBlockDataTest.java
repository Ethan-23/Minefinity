package org.evasive.me.minefinity.mining.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiningBlockDataTest {

    @Test
    void constructorTakesAnimationIdFirstThenProgress() {
        // Constructor order (animationID, progress) is the REVERSE of the field declaration order
        // (progress, animationID) — this guards against an accidental argument swap at call sites.
        MiningBlockData data = new MiningBlockData(42, 0.75f);

        assertEquals(42, data.getAnimationID());
        assertEquals(0.75f, data.getProgress(), 1e-6);
    }

    @Test
    void settersOverwriteBothFieldsIndependently() {
        MiningBlockData data = new MiningBlockData(1, 0f);
        data.setAnimationID(2);
        data.setProgress(3.5f);

        assertEquals(2, data.getAnimationID());
        assertEquals(3.5f, data.getProgress(), 1e-6);
    }

    @Test
    void progressIsANakedAccumulatorWithNoClampingOrValidation() {
        MiningBlockData data = new MiningBlockData(0, 0f);

        data.setProgress(-10f);
        assertEquals(-10f, data.getProgress(), 0f, "progress can go negative — nothing clamps it to 0");

        data.setProgress(500f);
        assertEquals(500f, data.getProgress(), 0f, "progress can exceed 1.0 — it is not a normalised fraction");
    }

    @Test
    void progressHappilyStoresNaN() {
        // A single NaN speed increment upstream would silently poison a block's progress forever, since
        // every comparison against NaN is false. Documents that nothing rejects it here.
        MiningBlockData data = new MiningBlockData(0, Float.NaN);
        assertTrue(Float.isNaN(data.getProgress()));
    }
}
