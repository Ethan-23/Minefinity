package org.evasive.me.minefinity.mining.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.evasive.me.minefinity.mining.utils.MiningProgress.*;
import static org.junit.jupiter.api.Assertions.*;

class MiningProgressTest {

    @Nested
    class HitProgress {

        @Test
        void normalSpeedIsSpeedOverTen() {
            // 20 speed / 10 = 2.0, well under the 25% cap for a 100 health block
            assertEquals(2.0f, hitProgress(20f, 100), 1e-6);
        }

        @Test
        void fractionalSpeedProducesFractionalProgress() {
            assertEquals(0.5f, hitProgress(5f, 100), 1e-6);
        }

        @Test
        void zeroSpeedMakesNoProgress() {
            assertEquals(0f, hitProgress(0f, 100), 0f);
        }

        @Test
        void progressIsCappedAtAQuarterOfHealthToPreventInstaBreak() {
            // 1000/10 = 100, but the cap is 100/4 = 25
            assertEquals(25f, hitProgress(1000f, 100), 1e-6);
        }

        @Test
        void speedExactlyAtTheCapReturnsTheCap() {
            // 250/10 = 25 == 100/4; min of equal values
            assertEquals(25f, hitProgress(250f, 100), 1e-6);
        }

        @Test
        void speedJustUnderTheCapIsNotClamped() {
            assertEquals(24f, hitProgress(240f, 100), 1e-6);
        }

        @Test
        void theCapScalesWithHealth() {
            // health 40 -> cap 10; 1000/10 = 100 -> clamped to 10
            assertEquals(10f, hitProgress(1000f, 40), 1e-6);
        }

        @Test
        void theCapUsesFloatDivisionNotIntegerDivision() {
            // health 10 -> cap must be 2.5, not 2 (10/4 as a float)
            assertEquals(2.5f, hitProgress(1000f, 10), 1e-6);
        }

        @Test
        void aSingleHealthBlockStillHasAFractionalCap() {
            // health 1 -> cap 0.25; a fast miner is clamped to a quarter of a heart of progress
            assertEquals(0.25f, hitProgress(1000f, 1), 1e-6);
        }
    }

    @Nested
    class StageStep {

        @Test
        void standardHealthDividesIntoTenStages() {
            assertEquals(10, stageStep(100));
        }

        @Test
        void healthOfExactlyTenGivesOnePerStage() {
            assertEquals(1, stageStep(10));
        }

        @Test
        void stepFloorsTowardsZero() {
            assertEquals(2, stageStep(29));   // 29/10 = 2
        }

        @Test
        void healthBelowTenIsGuardedToAtLeastOne() {
            // Without the Math.max guard this is 0, and callers dividing by it would throw.
            assertEquals(1, stageStep(9));
            assertEquals(1, stageStep(5));
            assertEquals(1, stageStep(1));
        }

        @Test
        void zeroHealthStillReturnsAtLeastOne() {
            assertEquals(1, stageStep(0), "guard must prevent a zero step / divide-by-zero downstream");
        }

        @Test
        void largeHealthScalesLinearly() {
            assertEquals(100, stageStep(1000));
        }
    }

    @Nested
    class CrossedStage {

        // health 100 -> step 10 for most of these

        @Test
        void theVeryFirstHitAlwaysCountsAsCrossing() {
            // currentProgress == 0 forces the initial crack to render even before a full stage is earned
            assertTrue(crossedStage(0f, 4f, 10));
        }

        @Test
        void firstHitCountsEvenWhenProgressStillRoundsToZero() {
            assertTrue(crossedStage(0f, 0f, 10), "the == 0 branch fires regardless of accrued progress");
        }

        @Test
        void stayingInsideTheSameStageBucketDoesNotCross() {
            // 3 -> 5 both live in bucket 0 (x/10 == 0)
            assertFalse(crossedStage(3f, 5f, 10));
        }

        @Test
        void movingIntoTheNextStageBucketCrosses() {
            // bucket 0 (8/10) -> bucket 1 (12/10)
            assertTrue(crossedStage(8f, 12f, 10));
        }

        @Test
        void landingExactlyOnAStageBoundaryCrosses() {
            // 9 -> 10; 10/10 == 1 > 9/10 == 0
            assertTrue(crossedStage(9f, 10f, 10));
        }

        @Test
        void jumpingSeveralStagesInOneHitCrosses() {
            // 5 -> 25 skips from bucket 0 to bucket 2
            assertTrue(crossedStage(5f, 25f, 10));
        }

        @Test
        void movingWithinAHigherBucketDoesNotCross() {
            // 11 -> 19 both live in bucket 1
            assertFalse(crossedStage(11f, 19f, 10));
        }

        @Test
        void comparisonUsesIntegerFloorNotFloatDivision() {
            // If this were float division, 19.9/10 (1.99) vs 20.1/10 (2.01) would compare as a cross
            // on almost every tick. With integer floor: 19 -> bucket 1, 20 -> bucket 2, so it crosses here...
            assertTrue(crossedStage(19.9f, 20.1f, 10));
            // ...but a fractional move that stays within a bucket must NOT cross (float division would say it does).
            assertFalse(crossedStage(12.3f, 15.9f, 10));
        }

        @Test
        void aStepOfOneTreatsEveryWholeUnitAsAStage() {
            assertTrue(crossedStage(3f, 4f, 1));
            assertFalse(crossedStage(3.2f, 3.9f, 1), "3.2 and 3.9 floor to the same unit");
        }
    }

    @Nested
    class CanBreak {

        @Test
        void higherBreakingPowerCanBreak() {
            assertTrue(canBreak(5, 3));
        }

        @Test
        void equalBreakingPowerCanBreak() {
            // The gate is >=, so a block that needs exactly the player's power is mineable.
            assertTrue(canBreak(3, 3));
        }

        @Test
        void lowerBreakingPowerCannotBreak() {
            assertFalse(canBreak(2, 5));
        }

        @Test
        void zeroPowerBlockIsAlwaysBreakable() {
            assertTrue(canBreak(0, 0));
            assertTrue(canBreak(1, 0));
        }

        @Test
        void aBlockRequiringPowerAPlayerLacksCannotBreak() {
            assertFalse(canBreak(0, 1));
        }
    }
}
