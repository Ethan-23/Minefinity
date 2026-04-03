package org.evasive.me.minefinity.towns;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.admin.commands.PacketRefresh;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.core.npcs.NpcBehaviorRegistry;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.playerdata.commands.MineData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.structures.RepeatingTick;
import org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil.commands.PickaxeAnvilCommand;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.BlacksmithNpc;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.ForgeRecipeManager;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.config.ForgeRecipeConfig;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.service.ForgeService;
import org.evasive.me.minefinity.towns.structures.forge.smelter.SmelterNpc;
import org.evasive.me.minefinity.towns.structures.forge.smelter.events.SmelterEvents;
import org.evasive.me.minefinity.towns.structures.forge.smelter.recipes.SmelterRecipeManager;
import org.evasive.me.minefinity.towns.structures.forge.smelter.recipes.config.SmelterRecipeConfig;
import org.evasive.me.minefinity.towns.structures.forge.smelter.service.SmelterHandler;
import org.evasive.me.minefinity.towns.structures.forge.smelter.service.SmelterService;
import org.evasive.me.minefinity.towns.structures.mines.miner.MinerNpc;
import org.evasive.me.minefinity.towns.structures.mines.miner.events.AutoMinerEvents;
import org.evasive.me.minefinity.towns.structures.mines.miner.service.AutoMinerService;
import org.evasive.me.minefinity.towns.structures.resourceblock.BlockMasterNpc;
import org.evasive.me.minefinity.towns.structures.resourceblock.commands.BlockCommands;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTypeRegistryService;
import org.evasive.me.minefinity.towns.structures.service.StructureService;
import org.evasive.me.minefinity.towns.structures.shops.GoblinTinkererNpc;
import org.evasive.me.minefinity.towns.structures.shops.merchant.MerchantNpc;
import org.evasive.me.minefinity.towns.structures.shops.merchant.service.MerchantHandler;
import org.evasive.me.minefinity.towns.structures.townhall.mayor.MayorNpc;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.EngineerNpc;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.WorkshopRecipeManager;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.config.WorkshopRecipeConfig;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.service.EngineerService;
import org.evasive.me.minefinity.towns.worldPackets.events.ChunkLoadingEvents;
import org.evasive.me.minefinity.core.worlds.GenerateCustomWorlds;
import org.evasive.me.minefinity.towns.worldPackets.events.PlayerMoveListener;
import org.evasive.me.minefinity.towns.worldPackets.events.PlayerMovePacketEvents;

public class TownModule {

    private final SmelterService smelterService;
    private SmelterRecipeManager smelterRecipeManager;
    private final ForgeService forgeService;
    private ForgeRecipeManager forgeRecipeManager;
    private final AutoMinerService autoMinerService;
    private final MerchantHandler merchantHandler;
    private final EngineerService engineerService;
    private final WorkshopRecipeManager workshopRecipeManager;
    private final StructureService structureService;
    private final EconomyService economyService;
    private final MilestoneService milestoneService;
    private final BlockTierService blockTierService;
    private final CustomItemRegistryService customItemRegistryService;
    private final ItemPickupService itemPickupService;
    private final BackpackService backpackService;
    private final RecipeService recipeService;
    private final SmelterHandler smelterHandler;
    private final NpcBehaviorRegistry npcBehaviorRegistry;

    public TownModule(PlayerDataService playerDataService, EconomyService economyService, CustomItemRegistryService customItemRegistryService, BackpackService backpackService, BlockTierService blockTierService, MilestoneService milestoneService, ItemPickupService itemPickupService, NpcBehaviorRegistry npcBehaviorRegistry, BlockTypeRegistry blockTypeRegistry, BlockTypeRegistryService blockTypeRegistryService) {

        this.customItemRegistryService = customItemRegistryService;

        this.recipeService = new RecipeService(backpackService, customItemRegistryService);

        //Smelter
        this.smelterRecipeManager = new SmelterRecipeManager();

        new SmelterRecipeConfig(Minefinity.getCore(), smelterRecipeManager);
        this.smelterService = new SmelterService(playerDataService, customItemRegistryService, smelterRecipeManager);
        this.smelterHandler = new SmelterHandler(smelterService, customItemRegistryService, itemPickupService);
        //Forge
        this.forgeRecipeManager = new ForgeRecipeManager();
        new ForgeRecipeConfig(Minefinity.getCore(), forgeRecipeManager);
        this.forgeService = new ForgeService(playerDataService, customItemRegistryService);

        //Main mining block
        this.blockTierService = blockTierService;
        this.milestoneService = milestoneService;

        //Auto miner
        this.autoMinerService = new AutoMinerService(playerDataService, customItemRegistryService, blockTypeRegistryService);

        //Engineer
        this.engineerService = new EngineerService(playerDataService);
        this.workshopRecipeManager = new WorkshopRecipeManager();
        new WorkshopRecipeConfig(Minefinity.getCore(), workshopRecipeManager);

        //Shops
        this.economyService = economyService;
        this.merchantHandler = new MerchantHandler(this.economyService, customItemRegistryService, backpackService);

        //Misc
        this.structureService = new StructureService(playerDataService);

        this.backpackService = backpackService;
        this.itemPickupService = itemPickupService;

        this.npcBehaviorRegistry = npcBehaviorRegistry;
    }

    public void enable(JavaPlugin plugin) {
        //Events

        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(new PlayerMovePacketEvents());

        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new ChunkLoadingEvents(structureService, blockTierService), plugin);
        pm.registerEvents(new AutoMinerEvents(autoMinerService), plugin);
        pm.registerEvents(new SmelterEvents(smelterService), plugin);
        pm.registerEvents(new PlayerMoveListener(structureService), plugin);

        //Starts machine automation
        new RepeatingTick(autoMinerService, smelterService).startAutomation();
        new MineData(Minefinity.getCore(), structureService, blockTierService);
        new PacketRefresh(structureService, blockTierService);
        new PickaxeAnvilCommand(customItemRegistryService);
        new BlockCommands(blockTierService, milestoneService, economyService, customItemRegistryService);

        createNpcBehaviors();
    }

    public void disable(){

    }

    private void createNpcBehaviors(){
        npcBehaviorRegistry.addBehavior("blacksmith", () -> new BlacksmithNpc(customItemRegistryService, forgeRecipeManager, forgeService, recipeService));
        npcBehaviorRegistry.addBehavior("blockmaster", () -> new BlockMasterNpc(blockTierService, milestoneService, economyService, customItemRegistryService));
        npcBehaviorRegistry.addBehavior("engineer", () -> new EngineerNpc(engineerService, workshopRecipeManager, backpackService, customItemRegistryService, recipeService));
        npcBehaviorRegistry.addBehavior("goblintinkerer", GoblinTinkererNpc::new);
        npcBehaviorRegistry.addBehavior("mayor", () -> new MayorNpc(customItemRegistryService, structureService, recipeService));
        npcBehaviorRegistry.addBehavior("merchant", () -> new MerchantNpc(customItemRegistryService, economyService, backpackService));
        npcBehaviorRegistry.addBehavior("miner", () -> new MinerNpc(customItemRegistryService, autoMinerService, blockTierService, itemPickupService));
        npcBehaviorRegistry.addBehavior("smelter", () -> new SmelterNpc(customItemRegistryService, smelterService, smelterHandler, smelterRecipeManager));
    }

    public NpcBehaviorRegistry getNpcBehaviorRegistry() {
        return npcBehaviorRegistry;
    }
}
