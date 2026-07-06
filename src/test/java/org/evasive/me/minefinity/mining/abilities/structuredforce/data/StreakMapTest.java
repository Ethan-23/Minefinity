package org.evasive.me.minefinity.mining.abilities.structuredforce.data;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StreakMapTest {

    private StreakMap map;
    private UUID player;

    @BeforeEach
    void setUp() {
        map = new StreakMap();
        player = UUID.randomUUID();
    }

    private BaseBlock block(String name, int health) {
        return new BaseBlock(name, Material.IRON_ORE, 2, health, "drop", null, 0f, List.of());
    }

    @Test
    void anUntrackedPlayerHasNoStreakAndNoBlock() {
        assertEquals(0, map.getStreak(player));
        assertNull(map.getBaseBlock(player));
    }

    @Test
    void firstStreakStartsAtOneAndRemembersTheBlock() {
        BaseBlock iron = block("Iron", 100);
        map.addStreak(player, iron);

        assertEquals(1, map.getStreak(player));
        assertEquals(iron, map.getBaseBlock(player));
    }

    @Test
    void miningTheSameBlockRepeatedlyGrowsTheStreak() {
        BaseBlock iron = block("Iron", 100);
        map.addStreak(player, iron);
        map.addStreak(player, iron);
        map.addStreak(player, iron);

        assertEquals(3, map.getStreak(player));
    }

    @Test
    void switchingToADifferentBlockResetsTheStreakToOne() {
        BaseBlock iron = block("Iron", 100);
        BaseBlock gold = block("Gold", 100);
        map.addStreak(player, iron);
        map.addStreak(player, iron);   // streak 2

        map.addStreak(player, gold);

        assertEquals(1, map.getStreak(player));
        assertEquals(gold, map.getBaseBlock(player));
    }

    @Test
    void streaksAreTrackedPerPlayer() {
        UUID other = UUID.randomUUID();
        BaseBlock iron = block("Iron", 100);

        map.addStreak(player, iron);
        map.addStreak(player, iron);
        map.addStreak(other, iron);

        assertEquals(2, map.getStreak(player));
        assertEquals(1, map.getStreak(other));
    }

    @Test
    void twoEqualButDistinctBlockInstancesContinueTheSameStreak() {
        // BaseBlock is a record, so equality is by value — two identical definitions keep the streak alive.
        map.addStreak(player, block("Iron", 100));
        map.addStreak(player, block("Iron", 100));

        assertEquals(2, map.getStreak(player));
    }

    @Test
    void sameNamedBlockWithDifferentDefinitionBreaksTheStreak() {
        // Streaks are keyed on the WHOLE BaseBlock record: a same-named block whose stats differ
        // (here: a different health value) is treated as a different block and resets the streak.
        map.addStreak(player, block("Iron", 100));
        map.addStreak(player, block("Iron", 999));

        assertEquals(1, map.getStreak(player),
                "documents fragility: any change to a block definition breaks an in-progress streak");
    }
}
