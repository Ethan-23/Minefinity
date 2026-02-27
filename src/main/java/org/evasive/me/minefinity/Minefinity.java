package org.evasive.me.minefinity;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.admin.commands.*;
import org.evasive.me.minefinity.admin.commands.gamemode.Gamemode;
import org.evasive.me.minefinity.admin.commands.gamemode.GamemodeCreative;
import org.evasive.me.minefinity.admin.commands.gamemode.GamemodeSpectator;
import org.evasive.me.minefinity.admin.commands.gamemode.GamemodeSurvival;
import org.evasive.me.minefinity.admin.events.VanishListener;
import org.evasive.me.minefinity.admin.commands.economy.Economy;
import org.evasive.me.minefinity.admin.service.VanishService;
import org.evasive.me.minefinity.anvil.commands.PickaxeAnvilCommand;
import org.evasive.me.minefinity.commands.Spawn;
import org.evasive.me.minefinity.core.events.ServerSpawnEvents;
import org.evasive.me.minefinity.core.gui.GuiListener;
import org.evasive.me.minefinity.core.service.SpawnService;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.framework.ItemGiver;
import org.evasive.me.minefinity.customItems.backpack.Backpacks;
import org.evasive.me.minefinity.customItems.backpack.events.ItemPickupListener;
import org.evasive.me.minefinity.customItems.backpack.events.OpenBackpackListener;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minefinity.core.RepeatingTick;
import org.evasive.me.minefinity.customItems.framework.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.types.FuelItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minefinity.customItems.types.ResourceItem;
import org.evasive.me.minefinity.database.DatabaseManager;
import org.evasive.me.minefinity.database.ServerDataHandler;
import org.evasive.me.minefinity.database.repository.PlayerRepository;
import org.evasive.me.minefinity.database.service.AutosaveService;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.economy.EconomyService;
import org.evasive.me.minefinity.commands.balance.Balance;
import org.evasive.me.minefinity.commands.Pay;
import org.evasive.me.minefinity.core.events.ServerJoinEvent;
import org.evasive.me.minefinity.forge.service.ForgeService;
import org.evasive.me.minefinity.miner.events.AutoMinerEvents;
import org.evasive.me.minefinity.miner.service.AutoMinerService;
import org.evasive.me.minefinity.mining.service.AnimationIDs;
import org.evasive.me.minefinity.mining.service.MiningDataMap;
import org.evasive.me.minefinity.mining.service.SelectedBlockMap;
import org.evasive.me.minefinity.mining.events.SwingPacketEvents;
import org.evasive.me.minefinity.npcs.NpcInstanceMap;
import org.evasive.me.minefinity.player.sevices.*;
import org.evasive.me.minefinity.resourceblock.commands.BlockCommands;
import org.evasive.me.minefinity.npcs.events.NpcLoadEvents;
import org.evasive.me.minefinity.scoreboard.Scoreboard;
import org.evasive.me.minefinity.smelter.events.SmelterEvents;
import org.evasive.me.minefinity.smelter.recipes.SmelterRecipeRegistry;
import org.evasive.me.minefinity.smelter.service.SmelterService;
import org.evasive.me.minefinity.town.service.TownService;
import org.evasive.me.minefinity.workshop.service.EngineerService;
import org.evasive.me.minefinity.worldPackets.events.BlockPacketEvents;
import org.evasive.me.minefinity.worldPackets.events.ChunkLoadingEvents;
import org.evasive.me.minefinity.worldPackets.events.PlayerMovePacketEvents;
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
    private SmelterService smelterService;
    private DirtyPlayerService dirtyPlayerService;
    private AutosaveService autosaveService;
    private SpawnService spawnService;

    private Scoreboard scoreboard;

    private VanishService vanishService;

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

        dirtyPlayerService = new DirtyPlayerService();
        serverDataHandler = new ServerDataHandler(playerManager, new PlayerRepository(), dirtyPlayerService);
        autosaveService = new AutosaveService(serverDataHandler);

        townService = new TownService(playerManager);
        blockTierService = new BlockTierService(playerManager);
        backpackService = new BackpackService(playerManager);
        economyService = new EconomyService(playerManager);
        milestoneService = new MilestoneService(playerManager);
        autoMinerService = new AutoMinerService(playerManager);
        forgeService = new ForgeService(playerManager);
        engineerService = new EngineerService(playerManager);
        smelterService = new SmelterService(playerManager);
        spawnService = new SpawnService();




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
        saveDefaultConfig();

        worldGuardCheck();
        com.github.retrooper.packetevents.PacketEvents.getAPI().init();

        vanishService = new VanishService();
        scoreboard = new Scoreboard();

        registerDataMaps();
        registerCustomItems();
        registerSmelterRecipes();
        registerEvents();
        registerCommands();



        try {
            databaseConnect();
            serverDataHandler.loadAll();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("FAILED LOAD ALL");
        }

        databaseManager.closePool();

        //Automated Functions
        new RepeatingTick().startAutomation();

        //Scoreboard
        scoreboard.repeatingScoreboardUpdate();

    }

    public void databaseConnect() throws SQLException {
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
        pluginManager.registerEvents(new OpenBackpackListener(), this);
        pluginManager.registerEvents(new ItemPickupListener(), this);
        pluginManager.registerEvents(new ServerJoinEvent(), this);
        pluginManager.registerEvents(new AutoMinerEvents(), this);
        pluginManager.registerEvents(new SmelterEvents(), this);
        pluginManager.registerEvents(new VanishListener(), this);
        pluginManager.registerEvents(new ServerSpawnEvents(spawnService), this);
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
        new Vanish();
        new PacketRefresh();
        new SetSpawn(spawnService);
        new Spawn();
        new Speed();
        new Invesee();
        new GamemodeSpectator();
        new GamemodeCreative();
        new GamemodeSurvival();
    }

    private void registerCustomItems(){
        CustomItemRegistry.registerEnumItems(ResourceItem.values());
        CustomItemRegistry.registerEnumItems(FuelItem.values());
        CustomItemRegistry.registerEnumItems(PickaxeItem.values());
        CustomItemRegistry.registerEnumItems(PickaxeComponent.values());
        CustomItemRegistry.registerEnumItems(Backpacks.values());
        CustomItemRegistry.init();
    }

    private void registerSmelterRecipes(){
        SmelterRecipeRegistry.loadRecipes();
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

    public SmelterService getSmelterService() {
        return smelterService;
    }

    public EngineerService getEngineerService() {
        return engineerService;
    }

    public static DatabaseManager getDatabaseManager(){
        return databaseManager;
    }

    public VanishService getVanishService(){
        return vanishService;
    }

    public Scoreboard getScoreboard(){
        return this.scoreboard;
    }

    public DirtyPlayerService getDirtyPlayerService(){
        return dirtyPlayerService;
    }

    public ServerDataHandler getServerDataHandler(){
        return serverDataHandler;
    }

    public AutosaveService getAutosaveService(){
        return autosaveService;
    }

    public SpawnService getSpawnService(){
        return spawnService;
    }

    @Override
    public void onDisable() {

        try {
            databaseConnect();
            serverDataHandler.saveDirty();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("FAILED TO SAVE");
            e.printStackTrace();
        }

        databaseManager.closePool();

        com.github.retrooper.packetevents.PacketEvents.getAPI().terminate();
    }



}
