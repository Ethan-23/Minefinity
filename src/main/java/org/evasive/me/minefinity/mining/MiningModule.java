package org.evasive.me.minefinity.mining;

import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.itembuilder.resolvers.PickaxeResolver;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRegistry;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.data.SelectedBlockMap;
import org.evasive.me.minefinity.mining.events.SwingPacketEvents;
import org.evasive.me.minefinity.mining.handlers.BlockProgressHandler;
import org.evasive.me.minefinity.mining.milestones.BlockMilestone;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.mining.milestones.MilestoneStatContributor;
import org.evasive.me.minefinity.mining.utils.AnimationIDs;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponentRegistry;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.StatContributorRegistry;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;
import org.evasive.me.minefinity.towns.worldPackets.events.BlockPacketEvents;

public class MiningModule {

    private final MiningDataMap miningDataMap;
    private final AnimationIDs animationIDs;
    private final SelectedBlockMap selectedBlockMap;
    private final MilestoneService milestoneService;
    private final BlockTierService blockTierService;
    private final CustomItemRegistryService customItemRegistryService;
    private final BlockTypeRegistryService blockTypeRegistryService;
    private final BlockProgressHandler blockProgressHandler;
    private final MiningAbilityRegistry miningAbilityRegistry;
    private final MiningAbilityRunner miningAbilityRunner;

    public MiningModule(PlayerDataService playerDataService, CustomItemRegistryService customItemRegistryService, BlockTypeRegistry blockTypeRegistry, ItemPickupService itemPickupService, PickaxeResolver pickaxeResolver, StatsService statsService, PlayerDataComponentRegistry componentRegistry, StatContributorRegistry statContributorRegistry) {
        this.animationIDs = new AnimationIDs();
        this.miningAbilityRegistry = new MiningAbilityRegistry(animationIDs);
        this.miningAbilityRunner = new MiningAbilityRunner(miningAbilityRegistry, pickaxeResolver);
        this.miningDataMap = new MiningDataMap(animationIDs);
        this.selectedBlockMap = new SelectedBlockMap();
        this.blockTypeRegistryService = new BlockTypeRegistryService(blockTypeRegistry);
        this.milestoneService = new MilestoneService(playerDataService, blockTypeRegistryService, statsService);
        this.blockTierService = new BlockTierService(playerDataService, customItemRegistryService, blockTypeRegistryService, miningDataMap, selectedBlockMap);
        //Should be changed to a service that calls a handler for everything other than progress calcs
        this.blockProgressHandler = new BlockProgressHandler(pickaxeResolver, miningAbilityRunner, blockTierService, milestoneService, miningDataMap, customItemRegistryService, itemPickupService, statsService);

        this.customItemRegistryService = customItemRegistryService;

        // Register mining's per-player data slice with playerdata
        componentRegistry.register("milestone_data", BlockMilestone.class, BlockMilestone::new);

        // Register mining's stat source with playerdata
        statContributorRegistry.register(new MilestoneStatContributor(playerDataService));
    }

    public void enable(){
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new SwingPacketEvents(customItemRegistryService, selectedBlockMap, miningDataMap, animationIDs, blockProgressHandler));
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new BlockPacketEvents());
    }

    public void disable(){

    }

    public MilestoneService getMilestoneService() {
        return milestoneService;
    }

    public BlockTierService getBlockTierService() {
        return blockTierService;
    }

    public BlockTypeRegistryService getBlockTypeRegistryService() {
        return blockTypeRegistryService;
    }

}
