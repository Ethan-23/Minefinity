package org.evasive.me.minefinity.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.forge.gui.ForgeGui;
import org.evasive.me.minefinity.npcs.framework.NpcBehavior;

public class BlacksmithNpc implements NpcBehavior {
    @Override
    public void onInteract(Player player) {
        new ForgeGui(player).open();
    }

    @Override
    public void onTick() {

    }
}
