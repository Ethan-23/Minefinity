package org.evasive.me.minefinity.mining.milestones;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.MilestoneTier;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.core.notifications.NotificationService;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.jspecify.annotations.NonNull;

public class MiningMilestoneNotifier {

    private final NotificationService notificationService;

    public MiningMilestoneNotifier(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void tierUp(Player player, String blockId, int oldTier, int newTier, @NonNull MilestoneTier tier) {
        notificationService.sound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
        notificationService.chat(player, "<yellow>" + TextConversions.formatItemName(blockId) + " <gold>Milestone " + oldTier + " -> " + newTier);

        tier.stats().forEach((s, amt) ->{
            Stats stat = Stats.getStat(s);
            if(stat == null)
                return;
            notificationService.chat(player, "  <gray>+" + amt + " " + stat.getDisplay());
        });
    }
}
