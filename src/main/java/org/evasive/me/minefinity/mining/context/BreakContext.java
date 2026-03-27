package org.evasive.me.minefinity.mining.context;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;

import java.util.UUID;

public class BreakContext {

    private final Player player;
    private final BaseBlock baseBlock;

    public BreakContext(Player player, BaseBlock baseBlock) {
        this.player = player;
        this.baseBlock = baseBlock;
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
}
