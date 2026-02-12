package org.evasive.me.minefinity;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.anvil.commands.PickaxeAnvilCommand;
import org.evasive.me.minefinity.core.gui.GuiListener;
import org.evasive.me.minefinity.customItems.ItemGiver;
import org.evasive.me.minefinity.customItems.backpack.Backpacks;
import org.evasive.me.minefinity.customItems.backpack.events.ItemPickupEvent;
import org.evasive.me.minefinity.customItems.backpack.events.OpenBackpackEvent;
import org.evasive.me.minefinity.customItems.commands.MPickaxeAdd;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minefinity.automation.AutomationTimer;
import org.evasive.me.minefinity.customItems.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.items.FuelItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minefinity.customItems.items.ResourceItem;
import org.evasive.me.minefinity.mining.AnimationIDs;
import org.evasive.me.minefinity.mining.MiningDataMap;
import org.evasive.me.minefinity.mining.SelectedBlockMap;
import org.evasive.me.minefinity.mining.SwingPacketEvents;
import org.evasive.me.minefinity.npcs.NpcInstanceMap;
import org.evasive.me.minefinity.resourceblock.block.BlockCommands;
import org.evasive.me.minefinity.customItems.commands.Mgive;
import org.evasive.me.minefinity.customItems.ItemMaker;
import org.evasive.me.minefinity.npcs.events.NpcLoadEvents;
import org.evasive.me.minefinity.npcs.commands.MSpawnNpc;
import org.evasive.me.minefinity.player.JoinEvent;
import org.evasive.me.minefinity.player.command.MData;
import org.evasive.me.minefinity.scoreboard.Scoreboard;
import org.evasive.me.minefinity.worldPackets.BlockPacketEvents;
import org.evasive.me.minefinity.worldPackets.ChunkLoadingEvents;
import org.evasive.me.minefinity.worldPackets.PlayerMovePacketEvents;
import org.evasive.me.minefinity.npcs.events.InteractEvent;
import org.evasive.me.minefinity.player.PlayerManager;

public final class Minefinity extends JavaPlugin {

    public static Minefinity core;
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

    public static Minefinity getCore() {
        return core;
    }

    @Override
    public void onDisable() {
        com.github.retrooper.packetevents.PacketEvents.getAPI().terminate();
    }


}
