package org.evasive.me.minefinity.mining;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.admin.service.VanishService;
import org.evasive.me.minefinity.core.notifications.NotificationService;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.AbilityNotifier;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRegistry;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.blocks.PlayerBlockTiers;
import org.evasive.me.minefinity.mining.blocks.listener.BlockTierListener;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.data.SelectedBlockMap;
import org.evasive.me.minefinity.mining.events.MiningQuitListener;
import org.evasive.me.minefinity.mining.events.SwingPacketListener;
import org.evasive.me.minefinity.mining.handlers.BlockBreakHandler;
import org.evasive.me.minefinity.mining.handlers.BlockBreakNotifier;
import org.evasive.me.minefinity.mining.handlers.BlockProgressHandler;
import org.evasive.me.minefinity.mining.milestones.MiningBlockMilestones;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneService;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneNotifier;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneStatContributor;
import org.evasive.me.minefinity.mining.scoreboard.ScoreboardService;
import org.evasive.me.minefinity.mining.scoreboard.listener.ScoreboardConnectListener;
import org.evasive.me.minefinity.mining.utils.AnimationIDs;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponentRegistry;
import org.evasive.me.minefinity.playerdata.economy.EconomyService;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.StatContributorRegistry;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;
import org.evasive.me.minefinity.towns.worldPackets.events.BlockPacketListener;

public class MiningModule {

    private final MiningDataMap miningDataMap;
    private final AnimationIDs animationIDs;
    private final SelectedBlockMap selectedBlockMap;
    private final MiningMilestoneService milestoneService;
    private final BlockTierService blockTierService;
    private final CustomItemRegistryService customItemRegistryService;
    private final BlockTypeRegistryService blockTypeRegistryService;
    private final BlockProgressHandler blockProgressHandler;
    private final ScoreboardService scoreboardService;
    private final PlayerDataService playerDataService;
    private final MiningAbilityRegistry miningAbilityRegistry;

    public MiningModule(PlayerDataService playerDataService, CustomItemRegistryService customItemRegistryService, BlockTypeRegistry blockTypeRegistry, ItemPickupService itemPickupService, StatsService statsService, PlayerDataComponentRegistry componentRegistry, StatContributorRegistry statContributorRegistry, NotificationService notificationService, EconomyService economyService, VanishService vanishService) {
        this.animationIDs = new AnimationIDs();
        this.miningAbilityRegistry = new MiningAbilityRegistry(new AbilityNotifier(notificationService));
        MiningAbilityRunner miningAbilityRunner = new MiningAbilityRunner(this.miningAbilityRegistry);
        this.miningDataMap = new MiningDataMap(animationIDs);
        this.selectedBlockMap = new SelectedBlockMap();
        this.blockTypeRegistryService = new BlockTypeRegistryService(blockTypeRegistry);
        this.milestoneService = new MiningMilestoneService(playerDataService, blockTypeRegistryService, statsService, new MiningMilestoneNotifier(notificationService));
        this.blockTierService = new BlockTierService(playerDataService, customItemRegistryService, blockTypeRegistryService, miningDataMap, selectedBlockMap);

        BlockBreakHandler blockBreakHandler = new BlockBreakHandler(customItemRegistryService, itemPickupService,milestoneService,blockTierService,miningDataMap, new BlockBreakNotifier(notificationService));
        this.blockProgressHandler = new BlockProgressHandler(miningAbilityRunner, blockTierService, miningDataMap, customItemRegistryService, statsService, blockBreakHandler);
        this.customItemRegistryService = customItemRegistryService;
        this.playerDataService = playerDataService;
        this.scoreboardService = new ScoreboardService(playerDataService, economyService, vanishService, blockTypeRegistry);

        // Register mining's per-player data slices with playerdata
        componentRegistry.register("milestone_data", MiningBlockMilestones.class, MiningBlockMilestones::new);
        componentRegistry.register("block_tiers", PlayerBlockTiers.class, PlayerBlockTiers::new);

        // Register mining's stat source with playerdata
        statContributorRegistry.register(new MiningMilestoneStatContributor(playerDataService, blockTypeRegistryService));
    }

    public void enable(JavaPlugin plugin){
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new SwingPacketListener(customItemRegistryService, selectedBlockMap, miningDataMap, animationIDs, blockProgressHandler));
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new BlockPacketListener());

        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new BlockTierListener(playerDataService, blockTypeRegistryService), plugin);
        pm.registerEvents(new ScoreboardConnectListener(scoreboardService), plugin);
        pm.registerEvents(new MiningQuitListener(selectedBlockMap, miningDataMap, miningAbilityRegistry), plugin);

        //repeating scoreboard update
        scoreboardService.repeatingScoreboardUpdate();
    }

    public void disable(){
        scoreboardService.stop();

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
