package org.evasive.me.minevolutionCore;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minevolutionCore.enchanting.enchantments.commands.EnchantListCommand;
import org.evasive.me.minevolutionCore.enchanting.enchantments.gui.EnchantGUIEvents;
import org.evasive.me.minevolutionCore.forge.gui.ForgeGUIEvents;
import org.evasive.me.minevolutionCore.npcs.chatHandlers.CustomCommandHandler;
import org.evasive.me.minevolutionCore.enchanting.runicMatrix.events.EnchantInteractEvent;
import org.evasive.me.minevolutionCore.enchanting.runicMatrix.MatrixManager;
import org.evasive.me.minevolutionCore.blocks.BlockCommands;
import org.evasive.me.minevolutionCore.blocks.gui.BlockGUIEvents;
import org.evasive.me.minevolutionCore.customItems.commands.ItemCommands;
import org.evasive.me.minevolutionCore.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.customItems.commands.TestCommands;
import org.evasive.me.minevolutionCore.enchanting.enchantments.commands.AdminEnchantCommand;
import org.evasive.me.minevolutionCore.npcs.NPCJoinEvent;
import org.evasive.me.minevolutionCore.player.PlayerDataCommads;
import org.evasive.me.minevolutionCore.worldPackets.BlockPacketEvents;
import org.evasive.me.minevolutionCore.worldPackets.ChunkLoadingEvents;
import org.evasive.me.minevolutionCore.worldPackets.PlayerMovePacketEvents;
import org.evasive.me.minevolutionCore.mining.MiningAnimation;
import org.evasive.me.minevolutionCore.npcs.InteractEvent;
import org.evasive.me.minevolutionCore.npcs.NPCManager;
import org.evasive.me.minevolutionCore.player.PlayerManager;

public final class MinevolutionCore extends JavaPlugin {

    private static MinevolutionCore core;
    private static PlayerManager playerManager;
    private static NPCManager npcManager;
    private static MatrixManager matrixManager;

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
        matrixManager = new MatrixManager();
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
        pluginManager.registerEvents(new EnchantGUIEvents(), this);
        pluginManager.registerEvents(new EnchantInteractEvent(), this);
        pluginManager.registerEvents(new NPCJoinEvent(), this);
        pluginManager.registerEvents(new ChunkLoadingEvents(), this);
        pluginManager.registerEvents(new BlockGUIEvents(), this);
        pluginManager.registerEvents(new ForgeGUIEvents(), this);

        //Managers
        npcManager = new NPCManager();

        //Commands
        new ItemCommands();
        new AdminEnchantCommand();
        new PlayerDataCommads();
        new BlockCommands();
        new TestCommands();
        new EnchantListCommand();
        this.getCommand("enchant_action_yes").setExecutor(new CustomCommandHandler());
        this.getCommand("enchant_action_no").setExecutor(new CustomCommandHandler());
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

    public static MatrixManager getMatrixManager(){return matrixManager;}
}
