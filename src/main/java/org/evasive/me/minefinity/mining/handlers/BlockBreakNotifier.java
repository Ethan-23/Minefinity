package org.evasive.me.minefinity.mining.handlers;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.CustomItemStack;
import org.evasive.me.minefinity.core.notifications.NotificationService;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;

public class BlockBreakNotifier {

    private final NotificationService notificationService;
    private final Sound SPECIAL_SOUND = Sound.BLOCK_SPAWNER_BREAK;
    private final static float VOLUME = 0.3f;
    private final static float PITCH = 0.3f;

    public BlockBreakNotifier(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void blockBreak(Player player, Material material, CustomItemStack customItemStack, Rarity rarity, boolean specialDrop, boolean fullInventory){

        Sound sound = specialDrop ? SPECIAL_SOUND : material.createBlockData().getSoundGroup().getBreakSound();
        notificationService.sound(player, sound, VOLUME, PITCH);

        if(fullInventory)
            return;

        notificationService.actionBar(player, "<green>+" + customItemStack.getAmount() + " " + TextConversions.buildRarityColor(customItemStack.getCustomItem(), rarity));
    }
}
