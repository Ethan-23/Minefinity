package org.evasive.me.minevolutionCore;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minevolutionCore.mining.blocks.BlockCommands;
import org.evasive.me.minevolutionCore.mining.blocks.gui.BlockGUIEvents;
import org.evasive.me.minevolutionCore.mining.customItems.ItemCommands;
import org.evasive.me.minevolutionCore.mining.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.mining.enchantments.commands.EnchantsCommand;
import org.evasive.me.minevolutionCore.npcs.NPCJoinEvent;
import org.evasive.me.minevolutionCore.player.PlayerDataCommads;
import org.evasive.me.minevolutionCore.worldPackets.BlockPacketEvents;
import org.evasive.me.minevolutionCore.worldPackets.ChunkLoadingEvents;
import org.evasive.me.minevolutionCore.worldPackets.PlayerMovePacketEvents;
//import org.evasive.me.minevolutionCore.mining.BlockBreakAnimationPackets;
import org.evasive.me.minevolutionCore.mining.animation.MiningAnimation;
import org.evasive.me.minevolutionCore.npcs.InteractEvent;
import org.evasive.me.minevolutionCore.npcs.NPCManager;
import org.evasive.me.minevolutionCore.player.PlayerManager;
import org.evasive.me.minevolutionCore.worldPackets.WorldJoinEvent;

public final class MinevolutionCore extends JavaPlugin {

    private static MinevolutionCore core;
    private static PlayerManager playerManager;
    private static NPCManager npcManager;

    @Override
    public void onLoad(){
        com.github.retrooper.packetevents.PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //On Bukkit, calling this here is essential, hence the name "load"
        com.github.retrooper.packetevents.PacketEvents.getAPI().load();

        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new MiningAnimation());
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new BlockPacketEvents());
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new InteractEvent());
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new PlayerMovePacketEvents());
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        core = this;
        playerManager = new PlayerManager();
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null){
            Bukkit.getConsoleSender().sendMessage("WorldGuard Found!");
        }else{
            Bukkit.getConsoleSender().sendMessage("WorldGuard Not Found! :(");
            getServer().getPluginManager().disablePlugin(this);
        }

        com.github.retrooper.packetevents.PacketEvents.getAPI().init();

        //Custom Items
        new ItemMaker().init();
        //Events
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new NPCJoinEvent(), this);
        pluginManager.registerEvents(new WorldJoinEvent(), this);
        pluginManager.registerEvents(new ChunkLoadingEvents(), this);
        pluginManager.registerEvents(new BlockGUIEvents(), this);

        //Managers
        npcManager = new NPCManager();

        //Commands
        new ItemCommands();
        new EnchantsCommand();
        new PlayerDataCommads();
        new BlockCommands();
    }

    @Override
    public void onDisable() {
        com.github.retrooper.packetevents.PacketEvents.getAPI().terminate();
    }


    public static MinevolutionCore getCore() {
        return core;
    }

    public static PlayerManager getPlayerManager(){
        return playerManager;
    }

    public static NPCManager getNpcManager(){
        return npcManager;
    }
}
