package org.evasive.me.minevolutionCore.mining;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.customItems.ItemGiver;
import org.evasive.me.minevolutionCore.resourceblock.block.BlockDataFunctions;
import org.evasive.me.minevolutionCore.resourceblock.BlockType;
import org.evasive.me.minevolutionCore.resourceblock.milestones.MilestoneFunctions;
import org.evasive.me.minevolutionCore.utils.Messages;

import static org.evasive.me.minevolutionCore.MinevolutionCore.miningMap;
import static org.evasive.me.minevolutionCore.MinevolutionCore.playerManager;
import static org.evasive.me.minevolutionCore.customItems.ItemNameBuilder.formatItemName;


public class BlockBreak {

    public void handleBlockBreak(Location location, Player player){

        String dropId = BlockDataFunctions.getMaterialDrop(player);

        int amount = 1; // Change later based on fortune ect

        playerManager.addBlocksMined(player, 1);
        playBreakSound(player);
        MilestoneFunctions.addMaterialMilestone(player, BlockType.values()[playerManager.getSelectedBlockTier(player)].getBlock().material());
        miningMap.removeBlockPos(location, player.getUniqueId());

        int overflow = MinevolutionCore.itemGiver.givePlayerDrops(player, dropId, amount);

        if(overflow > 0)return;

        sendActionBar(player, dropId, amount);

    }

    private void sendActionBar(Player player, String dropId, int amount){
        player.sendActionBar(Messages.parse("<green>+<gray>" + amount + " <#55FFFF>" + formatItemName(dropId)));//Change to rarity color in future
    }

    private void playBreakSound(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 0.3f);
    }



}
