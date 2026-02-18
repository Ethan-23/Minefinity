package org.evasive.me.minefinity.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.npcs.npc.NpcBehavior;
import org.evasive.me.minefinity.town.gui.MayorGui;

public class MayorNpc implements NpcBehavior {
    @Override
    public void onInteract(Player player) {
        new MayorGui(player).open();
    }

    @Override
    public void onTick() {

    }
}
