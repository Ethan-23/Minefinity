package org.evasive.me.minefinity.npcs.framework;

import org.bukkit.entity.Player;

public interface CombatNpcBehavior extends NpcBehavior{
    void onAttack(Player player);
}
