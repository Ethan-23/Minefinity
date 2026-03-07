package org.evasive.me.minefinity.ranks.events;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.evasive.me.minefinity.ranks.Rank;
import org.evasive.me.minefinity.ranks.RankManager;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jspecify.annotations.NonNull;

import java.util.UUID;


public class ChatEvent implements Listener, ChatRenderer {

    private final RankManager rankManager;

    public ChatEvent(RankManager rankManager) {
        this.rankManager = rankManager;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(this);
    }

    @Override
    public @NonNull Component render(Player source, Component sourceDisplayName, Component message, Audience viewer) {

        UUID uuid = source.getUniqueId();

        Rank staffRank = rankManager.getStaffRank(uuid);
        Rank donorRank = rankManager.getDonorRank(uuid);

        String staffRankPrefix = staffRank == null ? "" : staffRank.getPrefix() + " ";
        String donorRankPrefix = donorRank == null ? "" : donorRank.getPrefix() + " ";

        return TextConversions.parse("")
                .append(TextConversions.parse(staffRankPrefix + "<reset>"))
                .append(TextConversions.parse(donorRankPrefix + "<reset>"))
                .append(sourceDisplayName)
                .append(TextConversions.parse(": "))
                .append(message);
    }
}
