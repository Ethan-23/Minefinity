package org.evasive.me.minevolutionCore.npcs.npc;

import org.bukkit.entity.Player;

public interface CombatNpcBehavior extends NpcBehavior{
    void onAttack(Player player);
}
