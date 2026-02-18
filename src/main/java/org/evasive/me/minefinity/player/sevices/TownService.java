package org.evasive.me.minefinity.player.sevices;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.town.Structure;
import org.evasive.me.minefinity.worldPackets.MassBlockPacketSender;
import org.evasive.me.minefinity.worldPackets.StructurePallets;


public class TownService {

    private final PlayerManager playerManager;
    private final MassBlockPacketSender massBlockPacketSender = new MassBlockPacketSender();
    private final BlockTierService blockTierService = Minefinity.getCore().getBlockTierService();


    public final String TOWNHALL_REGION = "minefinity_townhall";
    public final String MERCHANT_REGION = "minefinity_merchant";


    public TownService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public int getStructureLevel(Player player, Structure structure) {
        return playerManager.get(player).getTown().getStructureLevelMap().get(structure);
    }

    public void setStructureLevel(Player player, Structure structure, int level){
        playerManager.get(player).getTown().getStructureLevelMap().put(structure, level);
        switch (structure) {
            case TOWNHALL:
                handleTownhallArea(player);
                break;
            case MERCHANT:
                handleMerchantArea(player);
                break;
        }
    }

    public void handleMerchantArea(Player player) {
        updateWorldPackets(player, MERCHANT_REGION, Structure.MERCHANT);
    }

    public void handleTownhallArea(Player player) {
        updateWorldPackets(player, TOWNHALL_REGION, Structure.TOWNHALL);
    }

    private void updateWorldPackets(Player player, String regionString, Structure structure){
        World world = player.getWorld();
        ProtectedRegion region = massBlockPacketSender.getRegion(world, regionString);
        massBlockPacketSender.createBlockReplacementMap(player, world, region, StructurePallets.values()[getStructureLevel(player, structure)].replacementMap);
    }

}
