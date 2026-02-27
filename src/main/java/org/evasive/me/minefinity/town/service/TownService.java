package org.evasive.me.minefinity.town.service;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.town.Structure;
import org.evasive.me.minefinity.worldPackets.service.MassBlockPacketSender;
import org.evasive.me.minefinity.worldPackets.StructurePallets;


public class TownService {

    private final PlayerManager playerManager;
    private final MassBlockPacketSender massBlockPacketSender = new MassBlockPacketSender();
    DirtyPlayerService dirtyPlayerService = Minefinity.getCore().getDirtyPlayerService();

    public final String TOWNHALL_REGION = "minefinity_townhall";
    public final String MERCHANT_REGION = "minefinity_merchant";
    public final String WORKSHOP_REGION = "minefinity_workshop";
    public final String FORGE_REGION = "minefinity_forge";


    public TownService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public int getStructureLevel(Player player, Structure structure) {
        return playerManager.get(player).getTown().getStructureLevel(structure);
    }

    public void setStructureLevel(Player player, Structure structure, int level){
        dirtyPlayerService.addDirtyPlayer(player);
        playerManager.get(player).getTown().setStructureLevel(structure, level);
        switch (structure) {
            case TOWNHALL:
                handleTownhallArea(player);
                break;
            case MERCHANT:
                handleMerchantArea(player);
                break;
            case WORKSHOP:
                handleWorkshopArea(player);
                break;
            case FORGE:
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
