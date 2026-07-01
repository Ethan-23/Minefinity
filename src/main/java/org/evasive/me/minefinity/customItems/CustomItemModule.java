package org.evasive.me.minefinity.customItems;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.customItems.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.backpack.events.ItemPickupListener;
import org.evasive.me.minefinity.customItems.backpack.events.OpenBackpackListener;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.itembuilder.command.CreateCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.command.DeleteCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.command.MineGive;
import org.evasive.me.minefinity.core.events.PlayerInputListener;
import org.evasive.me.minefinity.customItems.itembuilder.resolvers.PickaxeResolver;
import org.evasive.me.minefinity.customItems.registry.CustomItemLoader;
import org.evasive.me.minefinity.customItems.registry.config.ItemRegistryConfigManager;
import org.evasive.me.minefinity.customItems.registry.config.RegistryConfigHandler;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.customItems.stats.EquipmentStatContributor;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.StatContributorRegistry;

public class CustomItemModule {

    private final CustomItemRegistry customItemRegistry;
    private final RegistryConfigHandler registryConfigHandler;
    private final BackpackService backpackService;
    private final ItemPickupService itemPickupService;
    private final CustomItemRegistryService customItemRegistryService;
    private final PlayerInputListener playerInputListener;

    public CustomItemModule(PlayerDataService playerDataService, PlayerInputListener playerInputListener, StatContributorRegistry statContributorRegistry) {
        this.customItemRegistry = new CustomItemRegistry();

        ItemRegistryConfigManager itemRegistryConfigManager = new ItemRegistryConfigManager();

        registryConfigHandler = new RegistryConfigHandler(itemRegistryConfigManager);
        CustomItemRegistryService.init(customItemRegistry, registryConfigHandler);
        customItemRegistryService = CustomItemRegistryService.get();

        itemRegistryConfigManager.createItemRegistryConfig();
        new CustomItemLoader(customItemRegistry).registerConfigItems(itemRegistryConfigManager);
        this.backpackService = new BackpackService(playerDataService);
        this.itemPickupService = new ItemPickupService(customItemRegistryService, backpackService);
        this.playerInputListener = playerInputListener;

        // Register customItems' stat source with playerdata
        statContributorRegistry.register(new EquipmentStatContributor());
    }

    public void enable(JavaPlugin plugin) {

        //Events
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new OpenBackpackListener(customItemRegistryService, backpackService), plugin);
        //FUTURE ETHAN REMEMBER TO MOVE THIS
        pm.registerEvents(new ItemPickupListener(customItemRegistryService, itemPickupService), plugin);

        //Commands
        new MineGive(customItemRegistryService);
        new CreateCustomItem(customItemRegistryService, registryConfigHandler, playerInputListener);
        new DeleteCustomItem(customItemRegistryService, registryConfigHandler);
    }


    public void disable(){
        registryConfigHandler.saveEntireRegistry(customItemRegistry.getAllItems());
    }

    public BackpackService getBackpackService(){
        return backpackService;
    }

    public ItemPickupService getItemPickupService(){
        return itemPickupService;
    }

    public CustomItemRegistryService getCustomItemRegistryService(){
        return customItemRegistryService;
    }

    public PickaxeResolver getPickaxeResolver(){
        return customItemRegistryService.getPickaxeResolver();
    }

}
