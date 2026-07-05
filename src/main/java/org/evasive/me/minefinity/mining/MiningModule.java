package org.evasive.me.minefinity.mining;

import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.notifications.NotificationService;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRegistry;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.data.SelectedBlockMap;
import org.evasive.me.minefinity.mining.events.SwingPacketEvents;
import org.evasive.me.minefinity.mining.handlers.BlockBreakHandler;
import org.evasive.me.minefinity.mining.handlers.BlockProgressHandler;
import org.evasive.me.minefinity.mining.milestones.MiningBlockMilestones;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneService;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneNotifier;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneStatContributor;
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
    private final MiningMilestoneService milestoneService;
    private final BlockTierService blockTierService;
    private final CustomItemRegistryService customItemRegistryService;
    private final BlockTypeRegistryService blockTypeRegistryService;
    private final BlockProgressHandler blockProgressHandler;

    public MiningModule(PlayerDataService playerDataService, CustomItemRegistryService customItemRegistryService, BlockTypeRegistry blockTypeRegistry, ItemPickupService itemPickupService, StatsService statsService, PlayerDataComponentRegistry componentRegistry, StatContributorRegistry statContributorRegistry, NotificationService notificationService) {
        this.animationIDs = new AnimationIDs();
        MiningAbilityRegistry miningAbilityRegistry = new MiningAbilityRegistry(animationIDs);
        MiningAbilityRunner miningAbilityRunner = new MiningAbilityRunner(miningAbilityRegistry);
        this.miningDataMap = new MiningDataMap(animationIDs);
        this.selectedBlockMap = new SelectedBlockMap();
        this.blockTypeRegistryService = new BlockTypeRegistryService(blockTypeRegistry);
        this.milestoneService = new MiningMilestoneService(playerDataService, blockTypeRegistryService, statsService, new MiningMilestoneNotifier(notificationService));
        this.blockTierService = new BlockTierService(playerDataService, customItemRegistryService, blockTypeRegistryService, miningDataMap, selectedBlockMap);

        BlockBreakHandler blockBreakHandler = new BlockBreakHandler(customItemRegistryService, itemPickupService,milestoneService,blockTierService,miningDataMap, notificationService);
        this.blockProgressHandler = new BlockProgressHandler(miningAbilityRunner, blockTierService, miningDataMap, customItemRegistryService, statsService, blockBreakHandler);
        this.customItemRegistryService = customItemRegistryService;

        // Register mining's per-player data slice with playerdata
        componentRegistry.register("milestone_data", MiningBlockMilestones.class, MiningBlockMilestones::new);

        // Register mining's stat source with playerdata
        statContributorRegistry.register(new MiningMilestoneStatContributor(playerDataService));
    }

    public void enable(JavaPlugin plugin){
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new SwingPacketEvents(customItemRegistryService, selectedBlockMap, miningDataMap, animationIDs, blockProgressHandler));
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new BlockPacketEvents());
    }

    public void disable(){

    }

    public MiningMilestoneService getMilestoneService() {
        return milestoneService;
    }

    public BlockTierService getBlockTierService() {
        return blockTierService;
    }

    public BlockTypeRegistryService getBlockTypeRegistryService() {
        return blockTypeRegistryService;
    }

}
