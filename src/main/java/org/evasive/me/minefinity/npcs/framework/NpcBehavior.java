package org.evasive.me.minefinity.npcs.framework;

import org.bukkit.entity.Player;

public interface NpcBehavior {

    void onInteract(Player player);
    void onTick();
}
