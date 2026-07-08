package org.evasive.me.minefinity.mining.context;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BreakContextTest {

    @Test
    void recordAccessorsAndUuidHelperReflectTheComponents() {
        UUID uuid = UUID.randomUUID();
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(uuid);
        BaseBlock block = new BaseBlock("Iron", Material.IRON_ORE, 2, 100, "drop", null, 0f, List.of());
        StatsContext stats = new StatsContext();

        BreakContext context = new BreakContext(player, block, stats);

        assertSame(player, context.player());
        assertSame(block, context.baseBlock());
        assertSame(stats, context.statsContext());
        assertEquals(uuid, context.getUUID(), "getUUID() should delegate to the player's unique id");
    }

    @Test
    void getUuidBlowsUpWhenThePlayerIsNull() {
        BreakContext context = new BreakContext(null, null, null);
        assertThrows(NullPointerException.class, context::getUUID);
    }
}
