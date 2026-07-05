package org.evasive.me.minefinity.mining.milestones;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MiningBlockMilestoneTest {

    @Test
    void getUnaddedData() {
        MiningBlockMilestones blockMilestone = new MiningBlockMilestones();
        assertEquals(0, blockMilestone.getBlocksMined("Beans"));
        assertEquals(0, blockMilestone.getProgress("Beans"));
        assertEquals(0, blockMilestone.getTier("Beans"));
    }

    @Test
    void increasingProgressAccumulatesForABlock() {
        MiningBlockMilestones blockMilestone = new MiningBlockMilestones();
        blockMilestone.changeProgress("Beans", 5);
        blockMilestone.changeProgress("Beans", 3);
        assertEquals(8, blockMilestone.getProgress("Beans"));
    }

    @Test
    void changeProgressCanDecreaseWithANegativeAmount() {
        MiningBlockMilestones blockMilestone = new MiningBlockMilestones();
        blockMilestone.changeProgress("Beans", 5);
        blockMilestone.changeProgress("Beans", -3);
        assertEquals(2, blockMilestone.getProgress("Beans"));
    }

    @Test
    void changeProgressAllowsExactlyZero() {
        MiningBlockMilestones blockMilestone = new MiningBlockMilestones();
        blockMilestone.changeProgress("Beans", 5);
        blockMilestone.changeProgress("Beans", -5);
        assertEquals(0, blockMilestone.getProgress("Beans"));
    }

    @Test
    void changeProgressRejectsAChangeThatWouldGoBelowZero() {
        MiningBlockMilestones blockMilestone = new MiningBlockMilestones();
        blockMilestone.changeProgress("Beans", 5);
        blockMilestone.changeProgress("Beans", -6);   // would be -1
        assertEquals(5, blockMilestone.getProgress("Beans"),
                "a change that would go negative is rejected entirely, leaving the value unchanged (not clamped to 0)");
    }

    @Test
    void blocksAreTrackedIndependently() {
        MiningBlockMilestones blockMilestone = new MiningBlockMilestones();
        blockMilestone.changeProgress("Beans", 5);
        assertEquals(5, blockMilestone.getProgress("Beans"));
        assertEquals(0, blockMilestone.getProgress("Corn"));
    }

}
