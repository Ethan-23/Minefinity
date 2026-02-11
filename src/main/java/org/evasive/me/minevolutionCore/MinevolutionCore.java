package org.evasive.me.minevolutionCore;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minevolutionCore.anvil.commands.PickaxeAnvilCommand;
import org.evasive.me.minevolutionCore.core.gui.GuiListener;
import org.evasive.me.minevolutionCore.customItems.ItemGiver;
import org.evasive.me.minevolutionCore.customItems.backpack.Backpacks;
import org.evasive.me.minevolutionCore.customItems.backpack.events.ItemPickupEvent;
import org.evasive.me.minevolutionCore.customItems.backpack.events.OpenBackpackEvent;
import org.evasive.me.minevolutionCore.customItems.commands.MPickaxeAdd;
import org.evasive.me.minevolutionCore.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minevolutionCore.automation.AutomationTimer;
import org.evasive.me.minevolutionCore.customItems.CustomItemRegistry;
import org.evasive.me.minevolutionCore.customItems.items.FuelItem;
import org.evasive.me.minevolutionCore.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minevolutionCore.customItems.items.ResourceItem;
import org.evasive.me.minevolutionCore.mining.AnimationIDs;
import org.evasive.me.minevolutionCore.mining.MiningDataMap;
import org.evasive.me.minevolutionCore.mining.SelectedBlockMap;
import org.evasive.me.minevolutionCore.mining.SwingPacketEvents;
import org.evasive.me.minevolutionCore.npcs.NpcInstanceMap;
import org.evasive.me.minevolutionCore.resourceblock.block.BlockCommands;
import org.evasive.me.minevolutionCore.customItems.commands.Mgive;
import org.evasive.me.minevolutionCore.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.npcs.events.NpcLoadEvents;
import org.evasive.me.minevolutionCore.npcs.commands.MSpawnNpc;
import org.evasive.me.minevolutionCore.player.JoinEvent;
import org.evasive.me.minevolutionCore.player.command.MData;
import org.evasive.me.minevolutionCore.scoreboard.Scoreboard;
import org.evasive.me.minevolutionCore.worldPackets.BlockPacketEvents;
import org.evasive.me.minevolutionCore.worldPackets.ChunkLoadingEvents;
import org.evasive.me.minevolutionCore.worldPackets.PlayerMovePacketEvents;
import org.evasive.me.minevolutionCore.npcs.events.InteractEvent;
import org.evasive.me.minevolutionCore.player.PlayerManager;

public final class MinevolutionCore extends JavaPlugin {

    public static MinevolutionCore core;
    //Player Data
    public static PlayerManager playerManager;
    //Npc load data
    public static NpcInstanceMap npcInstanceMap;
    //Mining System
    public static AnimationIDs animationIDs;
    public static MiningDataMap miningMap;
    public static SelectedBlockMap selectedBlockMap;
    //Item Collection System
    public static ItemGiver itemGiver;

    @Override
    public void onLoad(){
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
        core = this;

        worldGuardCheck();
        com.github.retrooper.packetevents.PacketEvents.getAPI().init();

        registerDataMaps();
        registerCustomItems();
        registerEvents();
        registerCommands();

        //Automated Functions
        new AutomationTimer().startAutomation();

        //Scoreboard
        new Scoreboard().repeatingScoreboardUpdate();
    }

    private void registerDataMaps(){
        miningMap = new MiningDataMap();
        animationIDs = new AnimationIDs();
        playerManager = new PlayerManager();
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
        pluginManager.registerEvents(new JoinEvent(), this);
        pluginManager.registerEvents(new NpcLoadEvents(), this);
        pluginManager.registerEvents(new ChunkLoadingEvents(), this);
        pluginManager.registerEvents(new GuiListener(), this);
        pluginManager.registerEvents(new OpenBackpackEvent(), this);
        pluginManager.registerEvents(new ItemPickupEvent(), this);
    }

    private void registerCommands(){
        new Mgive();
        new MPickaxeAdd();
        new MData();
        new PickaxeAnvilCommand();
        new BlockCommands();
        new MSpawnNpc();
    }

    private void registerCustomItems(){
        CustomItemRegistry.registerEnumItems(ResourceItem.values());
        CustomItemRegistry.registerEnumItems(FuelItem.values());
        CustomItemRegistry.registerEnumItems(PickaxeItem.values());
        CustomItemRegistry.registerEnumItems(PickaxeComponent.values());
        CustomItemRegistry.registerEnumItems(Backpacks.values());
        new ItemMaker().init();
    }

    public static MinevolutionCore getCore() {
        return core;
    }

    @Override
    public void onDisable() {
        com.github.retrooper.packetevents.PacketEvents.getAPI().terminate();
    }


}
