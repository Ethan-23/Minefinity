package org.evasive.me.minefinity;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.CoreModule;
import org.evasive.me.minefinity.customItems.CustomItemModule;
import org.evasive.me.minefinity.mining.MiningModule;
import org.evasive.me.minefinity.playerdata.PlayerDataModule;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.TownModule;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;

public final class Minefinity extends JavaPlugin {

    public static Minefinity core;

    private PlayerDataModule playerModule;
    private PlayerDataService playerDataService;

    private CustomItemModule customItemModule;
    private BackpackService backpackService;

    private CoreModule coreModule;

    private MiningModule miningModule;

    private TownModule townModule;

    @Override
    public void onLoad(){
        core = this;
        //Needs to be called before any other packet events
        com.github.retrooper.packetevents.PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //On Bukkit, calling this here is essential, hence the name "load"
        com.github.retrooper.packetevents.PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        worldGuardCheck();
        com.github.retrooper.packetevents.PacketEvents.getAPI().init();
        saveDefaultConfig();
        playerModule = new PlayerDataModule();
        playerModule.enable(this);
        playerDataService = playerModule.getPlayerService();

        coreModule = new CoreModule(playerDataService);
        coreModule.enable(this);

        customItemModule = new CustomItemModule(
                playerDataService,
                coreModule.getCustomItemRegistry(),
                coreModule.getPlayerInputListener()
        );

        customItemModule.enable(this);

        backpackService = customItemModule.getBackpackService();

        miningModule = new MiningModule(
                playerDataService,
                customItemModule.getCustomItemRegistryService(),
                coreModule.getBlockTypeRegistry(),
                customItemModule.getItemPickupService(),
                customItemModule.getPickaxeResolver()
        );

        miningModule.enable();

        townModule = new TownModule(
                playerDataService,
                coreModule.getEconomyService(),
                customItemModule.getCustomItemRegistryService(),
                backpackService,
                miningModule.getBlockTierService(),
                miningModule.getMilestoneService(),
                customItemModule.getItemPickupService(),
                coreModule.getNpcBehaviorRegistry(),
                coreModule.getBlockTypeRegistry(),
                miningModule.getBlockTypeRegistryService()
        );

        townModule.enable(this);
    }

    private void worldGuardCheck(){
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null) return;
        getServer().getPluginManager().disablePlugin(this);
    }

    public static Minefinity getCore() {
        return core;
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
}