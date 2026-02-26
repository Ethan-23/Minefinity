package org.evasive.me.minefinity.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.miner.gui.main.MinerMainGui;
import org.evasive.me.minefinity.npcs.framework.NpcBehavior;

public class MinerNpc implements NpcBehavior {

    @Override
    public void onInteract(Player player) {
        new MinerMainGui(player).open();
    }

    @Override
    public void onTick() {

    }
}
