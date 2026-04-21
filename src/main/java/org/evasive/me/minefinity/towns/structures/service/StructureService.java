package org.evasive.me.minefinity.towns.structures.service;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.registry.StructureRegistry;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.data.TownData;
import org.evasive.me.minefinity.towns.structures.data.Structure;
import org.evasive.me.minefinity.towns.worldPackets.service.MassBlockPacketSender;
import org.evasive.me.minefinity.towns.worldPackets.StructurePallets;

import java.util.Collection;
import java.util.UUID;


public class StructureService {

    private final PlayerDataService playerDataService;
    private final MassBlockPacketSender massBlockPacketSender = new MassBlockPacketSender();
    private final StructureRegistry structureRegistry;

    public final String TOWNHALL_REGION = "WORLD_TOWNHALL";
    public final String MERCHANT_REGION = "WORLD_MERCHANT";
    public final String WORKSHOP_REGION = "WORLD_WORKSHOP";
    public final String FORGE_REGION = "WORLD_FORGE";


    public StructureService(PlayerDataService playerDataService, StructureRegistry structureRegistry) {
        this.playerDataService = playerDataService;
        this.structureRegistry = structureRegistry;
    }

    public TownData getTownData(UUID uuid) {
        return playerDataService.getPlayerData(uuid).getTownData();
    }

    public int getStructureLevel(Player player, Structure structure) {
        TownData townData = getTownData(player.getUniqueId());
        return switch (structure.id()) {
            case "WORLD_MERCHANT" -> townData.getMerchantLevel();
            case "WORLD_WORKSHOP" -> townData.getWorkshopLevel();
            case "WORLD_FORGE" -> townData.getForgeLevel();
            case "WORLD_TOWNHALL" -> townData.getTownhallLevel();
            default -> throw new IllegalStateException("Unexpected value: " + structure.id());
        };
    }

    public void setStructureLevel(Player player, Structure structure, int level){
        TownData townData = getTownData(player.getUniqueId());

        switch (structure.id()) {
            case "WORLD_TOWNHALL":
                townData.setTownhallLevel(level);
                handleTownhallArea(player);
                break;
            case "WORLD_MERCHANT":
                townData.setMerchantLevel(level);
                handleMerchantArea(player);
                break;
            case "WORLD_WORKSHOP":
                townData.setWorkshopLevel(level);
                handleWorkshopArea(player);
                break;
            case "WORLD_FORGE":
                townData.setForgeLevel(level);
                handleForgeArea(player);
                break;
        }
    }

    public void handleMerchantArea(Player player) {
        updateWorldPackets(player, MERCHANT_REGION, structureRegistry.getStructure("WORLD_MERCHANT"));
    }

    public void handleTownhallArea(Player player) {
        updateWorldPackets(player, TOWNHALL_REGION, structureRegistry.getStructure("WORLD_TOWNHALL"));
    }

    public void handleForgeArea(Player player) {
        updateWorldPackets(player, FORGE_REGION, structureRegistry.getStructure("WORLD_FORGE"));
    }

    public void handleWorkshopArea(Player player) {
        updateWorldPackets(player, WORKSHOP_REGION, structureRegistry.getStructure("WORLD_WORKSHOP"));
    }

    private void updateWorldPackets(Player player, String regionString, Structure structure){
        World world = player.getWorld();
        ProtectedRegion region = massBlockPacketSender.getRegion(world, regionString);
        if(region == null)
            return;
        massBlockPacketSender.createBlockReplacementMap(player, world, region, StructurePallets.values()[getStructureLevel(player, structure)].replacementMap);
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
