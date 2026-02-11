package org.evasive.me.minevolutionCore.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.forge.gui.ForgeGui;
import org.evasive.me.minevolutionCore.npcs.npc.NpcBehavior;

public class BlacksmithNpc implements NpcBehavior {
    @Override
    public void onInteract(Player player) {
        new ForgeGui(player).open();
    }

    @Override
    public void onTick() {

    }
}
