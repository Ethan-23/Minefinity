package org.evasive.me.minefinity.core.notifications;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.jspecify.annotations.NonNull;

public class NotificationService {
    public void chat(@NonNull Player p, String msg)      { p.sendMessage(TextConversions.parse(msg)); }
    public void actionBar(@NonNull Player p, String msg) { p.sendActionBar(TextConversions.parse(msg)); }
    public void sound(@NonNull Player p, Sound s, float vol, float pitch) { p.playSound(p.getLocation(), s, vol, pitch); }
    public void title(Player p, String title, String sub) { /* adventure Title */ }
}