package org.evasive.me.minefinity.playerdata.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.playerdata.model.PlayerRanks;
import org.evasive.me.minefinity.playerdata.service.RankService;

import java.util.UUID;

public class PlayerChatListener implements Listener {

    private final RankService rankService;

    public PlayerChatListener(RankService rankService) {
        this.rankService = rankService;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerRanks playerRanks = rankService.getRanks(uuid);

        e.renderer((source, sourceDisplayName, message, viewer) -> TextConversions.parse(
                playerRanks.getWeightedPrefix() + " " + source.getName() + "<reset><gray>:<reset> " + PlainTextComponentSerializer.plainText().serialize(message)
        ));
    }

}
