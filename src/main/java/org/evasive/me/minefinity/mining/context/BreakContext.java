package org.evasive.me.minefinity.mining.context;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;

import java.util.UUID;

public record BreakContext(Player player, BaseBlock baseBlock, StatsContext statsContext) {

    public UUID getUUID() {
        return player.getUniqueId();
    }

}
