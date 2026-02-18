package org.evasive.me.minefinity.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.npcs.npc.NpcBehavior;
import org.evasive.me.minefinity.workshop.EngineerGui;
import org.evasive.me.minefinity.workshop.WorkshopMode;

public class EngineerNpc implements NpcBehavior {
    @Override
    public void onInteract(Player player) {
        new EngineerGui(player, WorkshopMode.CARPENTRY).open();
    }

    @Override
    public void onTick() {

    }
}
