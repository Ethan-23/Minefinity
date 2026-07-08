package org.evasive.me.minefinity.mining.context;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HitContextTest {

    @Test
    void gettersExposeConstructorArgumentsAndUuidComesFromThePlayer() {
        UUID uuid = UUID.randomUUID();
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(uuid);
        BaseBlock block = new BaseBlock("Coal", Material.COAL_ORE, 1, 100, "drop", null, 0f, List.of());
        StatsContext stats = new StatsContext();

        HitContext context = new HitContext(player, block, stats);

        assertSame(player, context.getPlayer());
        assertSame(block, context.getBaseBlock());
        assertSame(stats, context.getStatsContext());
        assertEquals(uuid, context.getUUID(), "getUUID() should delegate to the player's unique id");
    }

    @Test
    void getUuidBlowsUpWhenThePlayerIsNull() {
        // getUUID() dereferences the player with no guard, so a null player surfaces as an NPE here
        // rather than at construction time.
        HitContext context = new HitContext(null, null, null);
        assertThrows(NullPointerException.class, context::getUUID);
    }
}
