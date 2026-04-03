package org.evasive.me.minefinity.mining.context;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;

import java.util.UUID;

public class BreakContext {

    private final Player player;
    private final BaseBlock baseBlock;
    private final StatsContext statsContext;

    public BreakContext(Player player, BaseBlock baseBlock, StatsContext statsContext) {
        this.player = player;
        this.baseBlock = baseBlock;
        this.statsContext = statsContext;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public BaseBlock getBaseBlock() {
        return baseBlock;
    }

    public StatsContext getStatsContext() {
        return statsContext;
    }
}
