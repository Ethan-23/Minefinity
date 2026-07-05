package org.evasive.me.minefinity.mining.handlers;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.notifications.NotificationService;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;

import static org.evasive.me.minefinity.core.utils.TextConversions.buildRarityColor;

public class BlockBreakNotifier {

    private final NotificationService notificationService;

    public BlockBreakNotifier(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void blockBreak(Player player, BaseCustomItem baseCustomItem, int amount, boolean specialDrop, boolean fullInventory){
        Sound sound = specialDrop ? Sound.BLOCK_SPAWNER_BREAK : Sound.BLOCK_STONE_BREAK;
        notificationService.sound(player, sound, 0.3f, 0.3f);

        if(fullInventory)
            return;

        notificationService.actionBar(player, "<green>+" + amount + " " + buildRarityColor(baseCustomItem.getID(), baseCustomItem.getRarity()));
    }
}
