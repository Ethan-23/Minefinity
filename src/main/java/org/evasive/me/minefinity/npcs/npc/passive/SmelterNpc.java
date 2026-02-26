package org.evasive.me.minefinity.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.smelter.gui.SmelterGui;

public class SmelterNpc implements NpcBehavior {
    @Override
    public void onInteract(Player player) {
        new SmelterGui(player).open();
    }

    @Override
    public void onTick() {

    }
}
