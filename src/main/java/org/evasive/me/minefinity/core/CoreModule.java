package org.evasive.me.minefinity.core;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.admin.commands.*;
import org.evasive.me.minefinity.core.admin.commands.gamemode.Gamemode;
import org.evasive.me.minefinity.core.admin.commands.gamemode.GamemodeCreative;
import org.evasive.me.minefinity.core.admin.commands.gamemode.GamemodeSpectator;
import org.evasive.me.minefinity.core.admin.commands.gamemode.GamemodeSurvival;
import org.evasive.me.minefinity.core.admin.commands.warp.DeleteWarp;
import org.evasive.me.minefinity.core.admin.commands.warp.SetWarp;
import org.evasive.me.minefinity.core.admin.events.VanishListener;
import org.evasive.me.minefinity.core.admin.service.VanishService;
import org.evasive.me.minefinity.core.config.LocationConfig;
import org.evasive.me.minefinity.core.events.ServerJoinEvent;
import org.evasive.me.minefinity.core.events.ServerSpawnEvents;
import org.evasive.me.minefinity.core.gui.GuiListener;
import org.evasive.me.minefinity.core.notifications.NotificationService;
import org.evasive.me.minefinity.core.npcs.NpcBehaviorRegistry;
import org.evasive.me.minefinity.core.npcs.NpcInstanceMap;
import org.evasive.me.minefinity.core.npcs.events.InteractEvent;
import org.evasive.me.minefinity.core.npcs.events.NpcLoadEvents;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.core.spawn.command.Spawn;
import org.evasive.me.minefinity.core.spawn.service.SpawnService;
import org.evasive.me.minefinity.core.warp.command.Warp;
import org.evasive.me.minefinity.core.warp.service.WarpService;
import org.evasive.me.minefinity.core.worlds.GenerateCustomWorlds;
import org.evasive.me.minefinity.core.events.PlayerInputListener;
import org.evasive.me.minefinity.core.registry.config.MiningBlockRegistryConfig;

public class CoreModule {


    private final VanishService vanishService;
    private final WarpService warpService;
    private final SpawnService spawnService;

    private final NpcInstanceMap npcInstanceMap;
    private final NpcBehaviorRegistry npcBehaviorRegistry;
    private final PlayerInputListener playerInputListener;
    private final BlockTypeRegistry blockTypeRegistry;
    private final NotificationService notificationService;

    public CoreModule() {
        new GenerateCustomWorlds().init();
        this.vanishService = new VanishService();
        this.blockTypeRegistry = new BlockTypeRegistry();
        new MiningBlockRegistryConfig(blockTypeRegistry);
        LocationConfig locationConfig = new LocationConfig();
        this.warpService = new WarpService(locationConfig);
        this.spawnService = new SpawnService(locationConfig);
        this.npcInstanceMap = new NpcInstanceMap();
        this.npcBehaviorRegistry = new NpcBehaviorRegistry();
        this.playerInputListener = new PlayerInputListener();
        this.notificationService = new NotificationService();

    }

    public void enable(JavaPlugin plugin) {
        //commands
        new Gamemode();
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
        pm.registerEvents(new ServerJoinEvent(vanishService), plugin);
        pm.registerEvents(new VanishListener(vanishService), plugin);
        pm.registerEvents(new ServerSpawnEvents(spawnService), plugin);
        pm.registerEvents(this.playerInputListener, plugin);
        pm.registerEvents(new NpcLoadEvents(npcInstanceMap), plugin);

    }

    public void disable(){
        warpService.saveWarpLocations();
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

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public VanishService getVanishService() {
        return vanishService;
    }
}
