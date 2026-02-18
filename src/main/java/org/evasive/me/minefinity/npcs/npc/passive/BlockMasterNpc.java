package org.evasive.me.minefinity.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.npcs.npc.NpcBehavior;
import org.evasive.me.minefinity.resourceblock.gui.BlockGui;

public class BlockMasterNpc implements NpcBehavior {

    @Override
    public void onInteract(Player player) {
        new BlockGui(player).open();
    }

    @Override
    public void onTick() {

    }

}
