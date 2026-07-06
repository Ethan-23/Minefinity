package org.evasive.me.minefinity.mining.abilities;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.notifications.NotificationService;

public class AbilityNotifier {

    private static final float VOLUME = 0.5f;
    private static final float PITCH = 0.5f;

    private final NotificationService notificationService;

    public AbilityNotifier(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void abilitySound(Player player, Sound sound){
        notificationService.sound(player, sound, VOLUME, PITCH);
    }

    public void abilityNotification(Player player, PickaxeAbilities pickaxeAbility, String message){
        notificationService.actionBar(player, pickaxeAbility.getAbilityNameDisplay() + message);
        if(pickaxeAbility.hasSound())
            abilitySound(player, Registry.SOUNDS.get(NamespacedKey.minecraft(pickaxeAbility.getSoundKey())));
    }
}
