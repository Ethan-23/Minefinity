package org.evasive.me.minefinity.towns.structures.service;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.towns.structures.registry.StructureRegistry;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.data.Regions;
import org.evasive.me.minefinity.towns.data.TownData;
import org.evasive.me.minefinity.towns.structures.data.Structure;
import org.evasive.me.minefinity.towns.worldPackets.service.MassBlockPacketSender;

import java.util.Collection;
import java.util.UUID;


public class StructureService {

    private final PlayerDataService playerDataService;
    private final MassBlockPacketSender massBlockPacketSender = new MassBlockPacketSender();
    private final StructureRegistry structureRegistry;

    public StructureService(PlayerDataService playerDataService, StructureRegistry structureRegistry) {
        this.playerDataService = playerDataService;
        this.structureRegistry = structureRegistry;
    }

    public TownData getTownData(UUID uuid) {
        return playerDataService.getPlayerData(uuid).get(TownData.class);
    }

    public int getStructureLevel(Player player, Structure structure) {
        TownData townData = getTownData(player.getUniqueId());
        return switch (structure.id()) {
            case Regions.MERCHANT -> townData.getMerchantLevel();
            case Regions.WORKSHOP -> townData.getWorkshopLevel();
            case Regions.FORGE    -> townData.getForgeLevel();
            case Regions.TOWNHALL -> townData.getTownhallLevel();
            default -> throw new IllegalStateException("Unexpected value: " + structure.id());
        };
    }

    public void setStructureLevel(Player player, Structure structure, int level){
        TownData townData = getTownData(player.getUniqueId());

        switch (structure.id()) {
            case Regions.TOWNHALL:
                townData.setTownhallLevel(level);
                handleTownhallArea(player);
                break;
            case Regions.MERCHANT:
                townData.setMerchantLevel(level);
                handleMerchantArea(player);
                break;
            case Regions.WORKSHOP:
                townData.setWorkshopLevel(level);
                handleWorkshopArea(player);
                break;
            case Regions.FORGE:
                townData.setForgeLevel(level);
                handleForgeArea(player);
                break;
        }
    }

    public void handleMerchantArea(Player player) {
        updateWorldPackets(player, Regions.MERCHANT, structureRegistry.getStructure(Regions.MERCHANT));
    }

    public void handleTownhallArea(Player player) {
        updateWorldPackets(player, Regions.TOWNHALL, structureRegistry.getStructure(Regions.TOWNHALL));
    }

    public void handleForgeArea(Player player) {
        updateWorldPackets(player, Regions.FORGE, structureRegistry.getStructure(Regions.FORGE));
    }

    public void handleWorkshopArea(Player player) {
        updateWorldPackets(player, Regions.WORKSHOP, structureRegistry.getStructure(Regions.WORKSHOP));

    }

    private void updateWorldPackets(Player player, String regionString, Structure structure){
        World world = player.getWorld();
        ProtectedRegion region = massBlockPacketSender.getRegion(world, regionString);
        if(region == null)
            return;

        massBlockPacketSender.showSchematic(player, structure.id(), region.getMinimumPoint());
        //massBlockPacketSender.createBlockReplacementMap(player, world, region, StructurePallets.values()[getStructureLevel(player, structure)].replacementMap);
    }

    public Collection<Structure> getStructures() {
        return structureRegistry.getStructureList();
    }

    public boolean isStructure(String structureName) {
        return structureRegistry.hasId(structureName);
    }

    public Structure getStructure(String structureName) {
        return structureRegistry.getStructure(structureName);
    }
}
