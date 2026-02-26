package org.evasive.me.minefinity.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.merchant.gui.MerchantGui;
import org.evasive.me.minefinity.npcs.framework.NpcBehavior;

public class MerchantNpc implements NpcBehavior {
    @Override
    public void onInteract(Player player) {
        new MerchantGui(player).open();
    }

    @Override
    public void onTick() {

    }

}
