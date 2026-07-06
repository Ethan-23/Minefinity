package org.evasive.me.minefinity;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.CoreModule;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.CustomItemModule;
import org.evasive.me.minefinity.mining.MiningModule;
import org.evasive.me.minefinity.playerdata.PlayerDataModule;
import org.evasive.me.minefinity.towns.TownModule;


public final class Minefinity extends JavaPlugin {

    private static Minefinity instance;

    private PlayerDataModule playerModule;

    private CustomItemModule customItemModule;

    private CoreModule coreModule;

    private MiningModule miningModule;

    private TownModule townModule;

    @Override
    public void onLoad(){
        instance = this;
        //Needs to be called before any other packet events
        com.github.retrooper.packetevents.PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //On Bukkit, calling this here is essential, hence the name "load"
        com.github.retrooper.packetevents.PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        if(!hasWorldGuard()) return;
        com.github.retrooper.packetevents.PacketEvents.getAPI().init();

        saveDefaultConfig();
        coreModule = new CoreModule();
        coreModule.enable(this);

        playerModule = new PlayerDataModule();
        playerModule.enable(this);

        customItemModule = new CustomItemModule(
                playerModule.getPlayerDataService(),
                coreModule.getPlayerInputListener(),
                playerModule.getStatContributorRegistry(),
                playerModule.getComponentRegistry()
        );
        customItemModule.enable(this);

        miningModule = new MiningModule(
                playerModule.getPlayerDataService(),
                customItemModule.getCustomItemRegistryService(),
                coreModule.getBlockTypeRegistry(),
                customItemModule.getItemPickupService(),
                playerModule.getStatsService(),
                playerModule.getComponentRegistry(),
                playerModule.getStatContributorRegistry(),
                coreModule.getNotificationService(),
                playerModule.getEconomyService(),
                coreModule.getVanishService()
        );

        miningModule.enable(this);

        townModule = new TownModule(
                playerModule.getPlayerDataService(),
                playerModule.getEconomyService(),
                customItemModule.getCustomItemRegistryService(),
                customItemModule.getBackpackService(),
                miningModule.getBlockTierService(),
                miningModule.getMilestoneService(),
                customItemModule.getItemPickupService(),
                coreModule.getNpcBehaviorRegistry(),
                coreModule.getBlockTypeRegistry(),
                miningModule.getBlockTypeRegistryService(),
                playerModule.getComponentRegistry()
        );

        townModule.enable(this);
    }

    private boolean hasWorldGuard(){
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null)
            return true;
        getServer().getPluginManager().disablePlugin(this);
        return false;
    }

    public static Minefinity getCore() {
        return instance;
    }

    @Override
    public void onDisable() {
        townModule.disable();
        miningModule.disable();
        customItemModule.disable();
        coreModule.disable();
        playerModule.disable();
        com.github.retrooper.packetevents.PacketEvents.getAPI().terminate();
    }

    public static void SendConsoleMessage(String message){
        Bukkit.getConsoleSender().sendMessage(TextConversions.parse("[Minefinity] " + message));
    }

    public static void SendLogMessage(String message){
        // Fall back to a plain logger when the plugin isn't loaded (e.g. unit tests) so this never NPEs.
        if (getCore() == null) {
            java.util.logging.Logger.getLogger("Minefinity").severe(message);
            return;
        }
        getCore().getLogger().severe(message);
    }
}