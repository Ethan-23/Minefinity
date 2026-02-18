package org.evasive.me.minefinity;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.admin.commands.*;
import org.evasive.me.minefinity.admin.commands.economy.Economy;
import org.evasive.me.minefinity.anvil.commands.PickaxeAnvilCommand;
import org.evasive.me.minefinity.core.gui.GuiListener;
import org.evasive.me.minefinity.customItems.ItemGiver;
import org.evasive.me.minefinity.customItems.backpack.Backpacks;
import org.evasive.me.minefinity.customItems.backpack.events.ItemPickupEvent;
import org.evasive.me.minefinity.customItems.backpack.events.OpenBackpackEvent;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minefinity.automation.AutomationTimer;
import org.evasive.me.minefinity.customItems.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.items.FuelItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minefinity.customItems.items.ResourceItem;
import org.evasive.me.minefinity.database.DatabaseManager;
import org.evasive.me.minefinity.database.ServerDataHandler;
import org.evasive.me.minefinity.database.repository.PlayerRepository;
import org.evasive.me.minefinity.economy.commands.balance.Balance;
import org.evasive.me.minefinity.economy.commands.Pay;
import org.evasive.me.minefinity.events.ServerJoinEvent;
import org.evasive.me.minefinity.mining.AnimationIDs;
import org.evasive.me.minefinity.mining.MiningDataMap;
import org.evasive.me.minefinity.mining.SelectedBlockMap;
import org.evasive.me.minefinity.mining.SwingPacketEvents;
import org.evasive.me.minefinity.npcs.NpcInstanceMap;
import org.evasive.me.minefinity.player.sevices.*;
import org.evasive.me.minefinity.resourceblock.BlockCommands;
import org.evasive.me.minefinity.customItems.ItemMaker;
import org.evasive.me.minefinity.npcs.events.NpcLoadEvents;
import org.evasive.me.minefinity.scoreboard.Scoreboard;
import org.evasive.me.minefinity.worldPackets.BlockPacketEvents;
import org.evasive.me.minefinity.worldPackets.ChunkLoadingEvents;
import org.evasive.me.minefinity.worldPackets.PlayerMovePacketEvents;
import org.evasive.me.minefinity.npcs.events.InteractEvent;
import org.evasive.me.minefinity.player.PlayerManager;

import java.sql.SQLException;

public final class Minefinity extends JavaPlugin {

    public static Minefinity core;
    //Player Data
    private PlayerManager playerManager;
    private BlockTierService blockTierService;
    private TownService townService;
    private BackpackService backpackService;
    private EconomyService economyService;
    private MilestoneService milestoneService;
    private AutoMinerService autoMinerService;
    private ForgeService forgeService;
    private EngineerService engineerService;

    private Vanish vanish;

    //Npc load data
    public static NpcInstanceMap npcInstanceMap;
    //Mining System
    public static AnimationIDs animationIDs;
    public static MiningDataMap miningMap;
    public static SelectedBlockMap selectedBlockMap;
    //Item Collection System
    public static ItemGiver itemGiver;


    private static final DatabaseManager databaseManager = new DatabaseManager();
    ServerDataHandler serverDataHandler;

    @Override
    public void onLoad(){
        core = this;
        playerManager = new PlayerManager();

        townService = new TownService(playerManager);
        blockTierService = new BlockTierService(playerManager);
        backpackService = new BackpackService(playerManager);
        economyService = new EconomyService(playerManager);
        milestoneService = new MilestoneService(playerManager);
        autoMinerService = new AutoMinerService(playerManager);
        forgeService = new ForgeService(playerManager);
        engineerService = new EngineerService(playerManager);

        com.github.retrooper.packetevents.PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //On Bukkit, calling this here is essential, hence the name "load"
        com.github.retrooper.packetevents.PacketEvents.getAPI().load();

        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new SwingPacketEvents());
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new BlockPacketEvents());
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new InteractEvent());
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new PlayerMovePacketEvents());

    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        worldGuardCheck();
        com.github.retrooper.packetevents.PacketEvents.getAPI().init();

        vanish = new Vanish();

        registerDataMaps();
        registerCustomItems();
        registerEvents();
        registerCommands();

        serverDataHandler = new ServerDataHandler(playerManager, new PlayerRepository());

        try {
            databaseConnect();
            serverDataHandler.loadAll();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("FAILED LOAD ALL");
        }

        databaseManager.closePool();

        //Automated Functions
        new AutomationTimer().startAutomation();

        //Scoreboard
        new Scoreboard().repeatingScoreboardUpdate();

    }

    private void databaseConnect() throws SQLException {
        databaseManager.setup("127.0.0.1", 3306, "minefinity", "admin", "jdf7tA@tf");
    }

    private void registerDataMaps(){
        miningMap = new MiningDataMap();
        animationIDs = new AnimationIDs();
        npcInstanceMap = new NpcInstanceMap();
        selectedBlockMap = new SelectedBlockMap();
        itemGiver = new ItemGiver();
    }


    private void worldGuardCheck(){
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null) return;
        getServer().getPluginManager().disablePlugin(this);
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new NpcLoadEvents(), this);
        pluginManager.registerEvents(new ChunkLoadingEvents(), this);
        pluginManager.registerEvents(new GuiListener(), this);
        pluginManager.registerEvents(new OpenBackpackEvent(), this);
        pluginManager.registerEvents(new ItemPickupEvent(), this);
        pluginManager.registerEvents(vanish, this);
        pluginManager.registerEvents(new ServerJoinEvent(), this);
    }

    private void registerCommands(){
        new MineGive();
        new MinePickaxeAdd();
        new MineData(this, townService, blockTierService);
        new Gamemode();
        new PickaxeAnvilCommand();
        new BlockCommands();
        new MineSpawn();
        new Balance();
        new Pay();
        new Economy();
        new StaffMode();
        new Rename();
        //new Vanish();
    }

    private void registerCustomItems(){
        CustomItemRegistry.registerEnumItems(ResourceItem.values());
        CustomItemRegistry.registerEnumItems(FuelItem.values());
        CustomItemRegistry.registerEnumItems(PickaxeItem.values());
        CustomItemRegistry.registerEnumItems(PickaxeComponent.values());
        CustomItemRegistry.registerEnumItems(Backpacks.values());
        new ItemMaker().init();
    }

    public static Minefinity getCore() {
        return core;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public BlockTierService getBlockTierService() {
        return blockTierService;
    }

    public TownService getTownService() {
        return townService;
    }

    public BackpackService getBackpackService() {
        return backpackService;
    }

    public EconomyService getEconomyService() {
        return economyService;
    }

    public MilestoneService getMilestoneService() {
        return milestoneService;
    }

    public AutoMinerService getAutoMinerService() {
        return autoMinerService;
    }

    public ForgeService getForgeService() {
        return forgeService;
    }

    public EngineerService getEngineerService() {
        return engineerService;
    }

    public static DatabaseManager getDatabaseManager(){
        return databaseManager;
    }

    public Vanish getVanish(){
        return vanish;
    }

    @Override
    public void onDisable() {

        try {
            databaseConnect();
            serverDataHandler.saveAll(); // save first
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("FAILED TO SAVE");
        }

        databaseManager.closePool();

        com.github.retrooper.packetevents.PacketEvents.getAPI().terminate();
    }



}
