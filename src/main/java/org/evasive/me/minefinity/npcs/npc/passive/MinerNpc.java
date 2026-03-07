package org.evasive.me.minefinity.npcs.npc.passive;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.miner.gui.main.MinerMainGui;
import org.evasive.me.minefinity.miner.gui.main.MinerMainHandler;
import org.evasive.me.minefinity.miner.service.AutoMinerService;
import org.evasive.me.minefinity.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.player.sevices.BlockTierService;

public class MinerNpc implements NpcBehavior {

    private final AutoMinerService autoMinerService = Minefinity.getCore().getAutoMinerService();
    private final BlockTierService blockTierService = Minefinity.getCore().getBlockTierService();

    @Override
    public void onInteract(Player player) {
        new MinerMainGui(player, autoMinerService, blockTierService).open();
    }

    @Override
    public void onTick() {

    }
}
