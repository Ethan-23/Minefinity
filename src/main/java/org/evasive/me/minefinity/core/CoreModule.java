package org.evasive.me.minefinity.core;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.admin.commands.*;
import org.evasive.me.minefinity.core.admin.commands.economy.Economy;
import org.evasive.me.minefinity.core.admin.commands.gamemode.Gamemode;
import org.evasive.me.minefinity.core.admin.commands.gamemode.GamemodeCreative;
import org.evasive.me.minefinity.core.admin.commands.gamemode.GamemodeSpectator;
import org.evasive.me.minefinity.core.admin.commands.gamemode.GamemodeSurvival;
import org.evasive.me.minefinity.core.admin.commands.warp.DeleteWarp;
import org.evasive.me.minefinity.core.admin.commands.warp.SetWarp;
import org.evasive.me.minefinity.core.admin.events.VanishListener;
import org.evasive.me.minefinity.core.admin.service.VanishService;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.core.economy.commands.Pay;
import org.evasive.me.minefinity.core.economy.commands.balance.Balance;
import org.evasive.me.minefinity.core.events.ServerJoinEvent;
import org.evasive.me.minefinity.core.events.ServerSpawnEvents;
import org.evasive.me.minefinity.core.gui.GuiListener;
import org.evasive.me.minefinity.core.npcs.NpcBehaviorRegistry;
import org.evasive.me.minefinity.core.npcs.NpcInstanceMap;
import org.evasive.me.minefinity.core.npcs.events.InteractEvent;
import org.evasive.me.minefinity.core.npcs.events.NpcLoadEvents;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.core.scoreboard.ScoreboardService;
import org.evasive.me.minefinity.core.spawn.command.Spawn;
import org.evasive.me.minefinity.core.spawn.service.SpawnService;
import org.evasive.me.minefinity.core.warp.command.Warp;
import org.evasive.me.minefinity.core.warp.service.WarpService;
import org.evasive.me.minefinity.core.worlds.GenerateCustomWorlds;
import org.evasive.me.minefinity.core.events.PlayerInputListener;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.core.registry.config.MiningBlockRegistryConfig;

public class CoreModule {

    private final ScoreboardService scoreboardService;
    private final VanishService vanishService;
    private final WarpService warpService;
    private final SpawnService spawnService;
    private final EconomyService economyService;
    private final NpcInstanceMap npcInstanceMap;
    private final NpcBehaviorRegistry npcBehaviorRegistry;
    private final PlayerInputListener playerInputListener;
    private final BlockTypeRegistry blockTypeRegistry;

    public CoreModule(PlayerDataService playerDataService) {
        new GenerateCustomWorlds().init();
        this.economyService = new EconomyService(playerDataService);
        this.vanishService = new VanishService();
        this.blockTypeRegistry = new BlockTypeRegistry();
        new MiningBlockRegistryConfig(blockTypeRegistry);
        this.scoreboardService = new ScoreboardService(playerDataService, economyService, vanishService, blockTypeRegistry);
        this.warpService = new WarpService();
        this.spawnService = new SpawnService();
        this.npcInstanceMap = new NpcInstanceMap();
        this.npcBehaviorRegistry = new NpcBehaviorRegistry();
        this.playerInputListener = new PlayerInputListener();

    }

    public void enable(JavaPlugin plugin) {
        //commands
        new Gamemode();
        new Balance(economyService);
        new Pay(economyService);
        new Economy(economyService);
        new StaffMode();
        new Rename();
        new Vanish(vanishService);
        new SetSpawn(spawnService);
        new Spawn(spawnService);
        new Speed();
        new Invsee();
        new GamemodeSpectator();
        new GamemodeCreative();
        new GamemodeSurvival();
        new SetWarp(warpService);
        new Warp(warpService);
        new DeleteWarp(warpService);
        new MineSpawn(npcInstanceMap);

        //events
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new InteractEvent(npcInstanceMap, npcBehaviorRegistry));

        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new GuiListener(), plugin);
        pm.registerEvents(new ServerJoinEvent(vanishService, scoreboardService), plugin);
        pm.registerEvents(new VanishListener(vanishService), plugin);
        pm.registerEvents(new ServerSpawnEvents(spawnService), plugin);
        pm.registerEvents(this.playerInputListener, plugin);
        pm.registerEvents(new NpcLoadEvents(npcInstanceMap), plugin);

        //repeating
        scoreboardService.repeatingScoreboardUpdate();
    }

    public void disable(){
        warpService.saveWarpLocations();
    }

    public EconomyService getEconomyService() {
        return economyService;
    }

    public PlayerInputListener getPlayerInputListener() {
        return playerInputListener;
    }

    public NpcInstanceMap getNpcInstanceMap() {
        return npcInstanceMap;
    }

    public NpcBehaviorRegistry getNpcBehaviorRegistry() {
        return npcBehaviorRegistry;
    }

    public BlockTypeRegistry getBlockTypeRegistry() {
        return blockTypeRegistry;
    }
}
