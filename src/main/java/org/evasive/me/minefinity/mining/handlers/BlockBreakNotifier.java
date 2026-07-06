package org.evasive.me.minefinity.mining.handlers;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.CustomItemStack;
import org.evasive.me.minefinity.core.notifications.NotificationService;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;

public class BlockBreakNotifier {

    private final NotificationService notificationService;

    public BlockBreakNotifier(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void blockBreak(Player player, CustomItemStack customItemStack, Rarity rarity, boolean specialDrop, boolean fullInventory){
        Sound sound = specialDrop ? Sound.BLOCK_SPAWNER_BREAK : Sound.BLOCK_STONE_BREAK;
        notificationService.sound(player, sound, 0.3f, 0.3f);

        if(fullInventory)
            return;

        notificationService.actionBar(player, "<green>+" + customItemStack.getAmount() + " " + TextConversions.buildRarityColor(customItemStack.getCustomItem(), rarity));
    }
}
