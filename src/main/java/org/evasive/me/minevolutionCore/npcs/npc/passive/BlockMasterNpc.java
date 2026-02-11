package org.evasive.me.minevolutionCore.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.npcs.npc.NpcBehavior;
import org.evasive.me.minevolutionCore.resourceblock.block.gui.BlockGui;

public class BlockMasterNpc implements NpcBehavior {

    @Override
    public void onInteract(Player player) {
        new BlockGui(player).open();
    }

    @Override
    public void onTick() {

    }

}
