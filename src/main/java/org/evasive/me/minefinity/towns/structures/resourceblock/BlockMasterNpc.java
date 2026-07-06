package org.evasive.me.minefinity.towns.structures.resourceblock;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.economy.EconomyService;
import org.evasive.me.minefinity.core.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneService;
import org.evasive.me.minefinity.towns.structures.resourceblock.gui.BlockGui;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;

public class BlockMasterNpc implements NpcBehavior {

    private final BlockTierService blockTierService;
    private final MiningMilestoneService milestoneService;
    private final EconomyService economyService;
    private final CustomItemRegistryService customItemRegistryService;

    public BlockMasterNpc(BlockTierService blockTierService, MiningMilestoneService milestoneService, EconomyService economyService, CustomItemRegistryService customItemRegistryService) {
        this.blockTierService = blockTierService;
        this.milestoneService = milestoneService;
        this.economyService = economyService;
        this.customItemRegistryService = customItemRegistryService;
    }

    @Override
    public void onInteract(Player player) {
        new BlockGui(player, blockTierService, customItemRegistryService, milestoneService, economyService, player.getWorld().getName()).open();
    }

    @Override
    public void onTick() {

    }

}
