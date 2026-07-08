package org.evasive.me.minefinity.mining.data;

import org.bukkit.block.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SelectedBlockMapTest {

    private SelectedBlockMap map;
    private UUID player;

    @BeforeEach
    void setUp() {
        map = new SelectedBlockMap();
        player = UUID.randomUUID();
    }

    @Test
    void anUntrackedPlayerHasNoSelection() {
        assertFalse(map.hasPlayer(player));
        assertNull(map.getSelectedBlock(player));
    }

    @Test
    void addThenReadBackTheSameBlockInstance() {
        Block block = mock(Block.class);
        map.addSelectedBlock(player, block);

        assertTrue(map.hasPlayer(player));
        assertSame(block, map.getSelectedBlock(player));
    }

    @Test
    void reAddingReplacesThePreviousSelection() {
        Block first = mock(Block.class);
        Block second = mock(Block.class);
        map.addSelectedBlock(player, first);
        map.addSelectedBlock(player, second);

        assertSame(second, map.getSelectedBlock(player));
    }

    @Test
    void removingClearsTheSelection() {
        map.addSelectedBlock(player, mock(Block.class));
        map.removePlayer(player);

        assertFalse(map.hasPlayer(player));
        assertNull(map.getSelectedBlock(player));
    }

    @Test
    void removingAnUnknownPlayerIsANoOp() {
        assertDoesNotThrow(() -> map.removePlayer(UUID.randomUUID()));
    }

    @Test
    void selectionsAreTrackedPerPlayer() {
        UUID other = UUID.randomUUID();
        Block mine = mock(Block.class);
        Block theirs = mock(Block.class);
        map.addSelectedBlock(player, mine);
        map.addSelectedBlock(other, theirs);

        assertSame(mine, map.getSelectedBlock(player));
        assertSame(theirs, map.getSelectedBlock(other));
    }

    @Test
    void aNullBlockIsRejectedBecauseTheBackingMapIsConcurrent() {
        // SelectedBlockMap is backed by a ConcurrentHashMap, which forbids null values — a plain
        // HashMap would have silently accepted this. Guards against a regression that swaps the map type.
        Block nullBlock = null;
        assertThrows(NullPointerException.class, () -> map.addSelectedBlock(player, nullBlock));
    }

    @Test
    void aNullPlayerKeyIsRejected() {
        assertThrows(NullPointerException.class, () -> map.addSelectedBlock(null, mock(Block.class)));
    }
}
