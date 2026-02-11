package org.evasive.me.minevolutionCore.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.merchant.MerchantGui;
import org.evasive.me.minevolutionCore.npcs.npc.NpcBehavior;

public class MerchantNpc implements NpcBehavior {
    @Override
    public void onInteract(Player player) {
        new MerchantGui(player).open();
    }

    @Override
    public void onTick() {

    }

}
