package org.evasive.me.minefinity.towns.structures.service;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.data.TownData;
import org.evasive.me.minefinity.towns.structures.data.Structure;
import org.evasive.me.minefinity.towns.worldPackets.service.MassBlockPacketSender;
import org.evasive.me.minefinity.towns.worldPackets.StructurePallets;

import java.util.UUID;


public class StructureService {

    private final PlayerDataService playerDataService;
    private final MassBlockPacketSender massBlockPacketSender = new MassBlockPacketSender();

    public final String TOWNHALL_REGION = "minefinity_townhall";
    public final String MERCHANT_REGION = "minefinity_merchant";
    public final String WORKSHOP_REGION = "minefinity_workshop";
    public final String FORGE_REGION = "minefinity_forge";


    public StructureService(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    public TownData getTownData(UUID uuid) {
        return playerDataService.getPlayerData(uuid).getTownData();
    }

    public int getStructureLevel(Player player, Structure structure) {
        TownData townData = getTownData(player.getUniqueId());
        return switch (structure) {
            case MERCHANT -> townData.getMerchantLevel();
            case WORKSHOP -> townData.getWorkshopLevel();
            case FORGE -> townData.getForgeLevel();
            case TOWNHALL -> townData.getTownhallLevel();
        };
    }

    public void setStructureLevel(Player player, Structure structure, int level){
        TownData townData = getTownData(player.getUniqueId());

        switch (structure) {
            case TOWNHALL:
                townData.setTownhallLevel(level);
                handleTownhallArea(player);
                break;
            case MERCHANT:
                townData.setMerchantLevel(level);
                handleMerchantArea(player);
                break;
            case WORKSHOP:
                townData.setWorkshopLevel(level);
                handleWorkshopArea(player);
                break;
            case FORGE:
                townData.setForgeLevel(level);
                handleForgeArea(player);
                break;
        }
    }

    public void handleMerchantArea(Player player) {
        updateWorldPackets(player, MERCHANT_REGION, Structure.MERCHANT);
    }

    public void handleTownhallArea(Player player) {
        updateWorldPackets(player, TOWNHALL_REGION, Structure.TOWNHALL);
    }

    public void handleForgeArea(Player player) {
        updateWorldPackets(player, FORGE_REGION, Structure.FORGE);
    }

    public void handleWorkshopArea(Player player) {
        updateWorldPackets(player, WORKSHOP_REGION, Structure.WORKSHOP);
    }

    private void updateWorldPackets(Player player, String regionString, Structure structure){
        World world = player.getWorld();
        ProtectedRegion region = massBlockPacketSender.getRegion(world, regionString);
        if(region == null)
            return;
        massBlockPacketSender.createBlockReplacementMap(player, world, region, StructurePallets.values()[getStructureLevel(player, structure)].replacementMap);
    }

}
