package org.evasive.me.minefinity.core.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.utils.TextConversions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerInputListener implements Listener {

    private final Map<UUID, Consumer<String>> waitingPlayers = new HashMap<>();

    public void requestInput(Player player, Consumer<String> callback) {
        waitingPlayers.put(player.getUniqueId(), callback);
        player.sendMessage(TextConversions.parse("<gold>Please type your value in chat. Type <red>cancel <gold>to stop."));
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        UUID id = player.getUniqueId();

        if (waitingPlayers.containsKey(id)) {
            e.setCancelled(true);
            Component messageComponent = e.message();
            String message = PlainTextComponentSerializer.plainText().serialize(messageComponent);

            Consumer<String> callback = waitingPlayers.remove(id);
            Bukkit.getScheduler().runTask(Minefinity.getCore(), () -> callback.accept(message)); // run sync
        }
    }

}
