package org.evasive.me.minefinity.npcs.npc;

import org.bukkit.entity.Player;

public interface NpcBehavior {

    void onInteract(Player player);
    void onTick();
}
