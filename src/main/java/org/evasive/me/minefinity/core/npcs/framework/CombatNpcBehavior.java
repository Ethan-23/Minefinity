package org.evasive.me.minefinity.core.npcs.framework;

import org.bukkit.entity.Player;

public interface CombatNpcBehavior extends NpcBehavior{
    void onAttack(Player player);
}
