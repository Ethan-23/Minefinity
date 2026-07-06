package org.evasive.me.minefinity.mining.utils;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.data.CustomItemStack;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.evasive.me.minefinity.mining.utils.MiningDrops.*;
import static org.junit.jupiter.api.Assertions.*;

class MiningDropsTest {

    private static final String DROP_ID = "drop";
    private static final String SPECIAL_DROP_ID = "specialDrop";

    private static BaseBlock plainBlock() {
        return new BaseBlock("Stone", Material.STONE, 1, 100, DROP_ID, null, 0f, List.of());
    }

    private static BaseBlock specialBlock() {
        return new BaseBlock("Stone", Material.STONE, 1, 100, DROP_ID, SPECIAL_DROP_ID, 0f, List.of());
    }

    private static BaseBlock noDropBlock() {
        return new BaseBlock("Broken", Material.STONE, 1, 100, null, null, 0f, List.of());
    }

    @Nested
    class CalculateFortuneDrops {

        @Test
        void zeroFortuneAddsNoBonus() {
            assertEquals(0, calculateFortuneDrops(0f));
        }

        @Test
        void wholeHundredsAreFullyGuaranteed() {
            // remainder is 0, so the chance roll can never add anything
            assertEquals(1, calculateFortuneDrops(100f));
            assertEquals(3, calculateFortuneDrops(300f));
        }

        @Test
        void aFractionalRemainderStaysWithinItsGuaranteedAndChanceBounds() {
            // 250 -> 2 guaranteed + a 50% roll for one more == always 2 or 3
            for (int i = 0; i < 500; i++) {
                int bonus = calculateFortuneDrops(250f);
                assertTrue(bonus == 2 || bonus == 3, "250 fortune should yield 2 or 3, got " + bonus);
            }
        }
    }

    @Nested
    class IsSpecialDrop {

        @Test
        void aBlockWithoutASpecialDropIsNeverSpecial() {
            // full chance, but the block has no special drop id to give
            assertFalse(isSpecialDrop(plainBlock(), 100));
        }

        @Test
        void fullChanceAlwaysRollsSpecial() {
            for (int i = 0; i < 200; i++) {
                assertTrue(isSpecialDrop(specialBlock(), 100));
            }
        }

        @Test
        void zeroChanceNeverRollsSpecial() {
            // the roll is 1..100, so <= 0 can never succeed even with a special drop present
            for (int i = 0; i < 200; i++) {
                assertFalse(isSpecialDrop(specialBlock(), 0));
            }
        }
    }

    @Nested
    class AddBlockDrops {

        @Test
        void aNormalBreakAddsTheBlockDropWithASingleBaseItem() {
            List<CustomItemStack> drops = new ArrayList<>();

            boolean special = addBlockDrops(drops, plainBlock(), 0f, 1);

            assertFalse(special);
            assertEquals(1, drops.size());
            assertEquals(DROP_ID, drops.getFirst().getCustomItem());
            assertEquals(1, drops.getFirst().getAmount());
        }

        @Test
        void aSpecialBreakAddsTheSpecialDropId() {
            List<CustomItemStack> drops = new ArrayList<>();

            boolean special = addBlockDrops(drops, specialBlock(), 0f, 100);

            assertTrue(special);
            assertEquals(SPECIAL_DROP_ID, drops.getFirst().getCustomItem());
        }

        @Test
        void fortuneRaisesTheDropAmount() {
            List<CustomItemStack> drops = new ArrayList<>();

            addBlockDrops(drops, plainBlock(), 100f, 1);

            assertEquals(2, drops.getFirst().getAmount());   // 1 base + 1 guaranteed fortune
        }

        @Test
        void aMissingDropIdAddsNothing() {
            List<CustomItemStack> drops = new ArrayList<>();

            boolean special = addBlockDrops(drops, noDropBlock(), 0f, 1);

            assertFalse(special);
            assertTrue(drops.isEmpty(), "a block with no drop id must not add a null-id stack");
        }
    }
}
