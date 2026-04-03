package org.evasive.me.minefinity.towns.structures.mines.miner;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.mines.miner.gui.main.MinerMainGui;
import org.evasive.me.minefinity.towns.structures.mines.miner.service.AutoMinerService;
import org.evasive.me.minefinity.core.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;

public class MinerNpc implements NpcBehavior {

    private final CustomItemRegistryService customItemRegistryService;
    private final AutoMinerService autoMinerService;
    private final BlockTierService blockTierService;
    private final ItemPickupService itemPickupService;

    public MinerNpc(CustomItemRegistryService customItemRegistryService, AutoMinerService autoMinerService, BlockTierService blockTierService, ItemPickupService itemPickupService) {
        this.customItemRegistryService = customItemRegistryService;
        this.autoMinerService = autoMinerService;
        this.blockTierService = blockTierService;
        this.itemPickupService =  itemPickupService;
    }

    @Override
    public void onInteract(Player player) {
        new MinerMainGui(player, customItemRegistryService, itemPickupService, autoMinerService, blockTierService).open();
    }

    @Override
    public void onTick() {

    }
}
