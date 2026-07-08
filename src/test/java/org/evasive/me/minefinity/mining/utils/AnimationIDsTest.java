package org.evasive.me.minefinity.mining.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimationIDsTest {

    @Test
    void freshIdsCountDownFromIntegerMax() {
        AnimationIDs pool = new AnimationIDs();
        assertEquals(Integer.MAX_VALUE,     pool.getUniqueAnimationId());
        assertEquals(Integer.MAX_VALUE - 1, pool.getUniqueAnimationId());
        assertEquals(Integer.MAX_VALUE - 2, pool.getUniqueAnimationId());
    }

    @Test
    void releasedIdIsReusedBeforeFreshOnes() {
        AnimationIDs pool = new AnimationIDs();
        int first = pool.getUniqueAnimationId();   // MAX_VALUE
        pool.releaseAnimationId(first);
        assertEquals(first, pool.getUniqueAnimationId(),
                "a released id should be recycled before a brand-new one is issued");
    }

    @Test
    void recycledIdsComeBackInFifoOrder() {
        AnimationIDs pool = new AnimationIDs();
        pool.releaseAnimationId(10);
        pool.releaseAnimationId(20);
        assertEquals(10, pool.getUniqueAnimationId(), "first released should return first");
        assertEquals(20, pool.getUniqueAnimationId(), "then the second");
    }

    @Test
    void fallsBackToFreshSequenceOnceRecycledPoolIsEmpty() {
        AnimationIDs pool = new AnimationIDs();
        pool.releaseAnimationId(42);
        assertEquals(42, pool.getUniqueAnimationId());               // recycled first
        assertEquals(Integer.MAX_VALUE, pool.getUniqueAnimationId()); // then back to fresh
    }

    @Test
    void releasingTheSameIdTwiceIsDedupedSoTwoCallersNeverShareIt() {
        AnimationIDs pool = new AnimationIDs();
        pool.releaseAnimationId(7);
        pool.releaseAnimationId(7);   // the trackQueue guard drops this duplicate release

        // Only one copy of 7 is recycled, so the second caller falls through to a fresh id instead of
        // colliding on 7.
        assertEquals(7, pool.getUniqueAnimationId(), "the single recycled copy comes back first");
        assertEquals(Integer.MAX_VALUE, pool.getUniqueAnimationId(),
                "there is no second copy of 7 — the next caller gets a fresh id");
    }

}
