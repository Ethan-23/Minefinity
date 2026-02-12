package org.evasive.me.minefinity.mining;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.resourceblock.block.BlockDataFunctions;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.resourceblock.milestones.MilestoneFunctions;
import org.evasive.me.minefinity.utils.Messages;

import static org.evasive.me.minefinity.Minefinity.miningMap;
import static org.evasive.me.minefinity.Minefinity.playerManager;
import static org.evasive.me.minefinity.customItems.ItemNameBuilder.formatItemName;


public class BlockBreak {

    public void handleBlockBreak(Location location, Player player){

        String dropId = BlockDataFunctions.getMaterialDrop(player);

        int amount = 1; // Change later based on fortune ect

        playerManager.addBlocksMined(player, 1);
        playBreakSound(player);
        MilestoneFunctions.addMaterialMilestone(player, BlockType.values()[playerManager.getSelectedBlockTier(player)].getBlock().material());
        miningMap.removeBlockPos(location, player.getUniqueId());

        int overflow = Minefinity.itemGiver.givePlayerDrops(player, dropId, amount);

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
