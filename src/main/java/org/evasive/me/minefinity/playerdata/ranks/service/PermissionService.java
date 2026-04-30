package org.evasive.me.minefinity.playerdata.ranks.service;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.lunar.LunarNametag;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.playerdata.model.PlayerRanks;
import org.evasive.me.minefinity.playerdata.model.Rank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionService {

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();

    Minefinity plugin;

    public PermissionService(Minefinity plugin) {
        this.plugin = plugin;
    }

    public void applyPermissions(Player player, PlayerRanks ranks) {

        PermissionAttachment oldAttachment = attachments.remove(player.getUniqueId());

        if (oldAttachment != null) {
            try {
                player.removeAttachment(oldAttachment);
            } catch (IllegalArgumentException ignored) {
                // It wasn't attached to this player anymore
            }
        }

        PermissionAttachment newAttachment = player.addAttachment(plugin);

        Component playerDisplayName = TextConversions.parse(player.getName());

        Rank staffRank = ranks.getStaffRank();
        Rank donorRank = ranks.getDonorRank();

        if(ranks.getStaffRankId() != null){
            playerDisplayName = TextConversions.parse(ranks.getStaffRank().getPrefix() + " <reset>" + player.getName());
            Team team = scoreboard.getTeam(staffRank.getId());
            if(team != null)
                team.addPlayer(player);
        }else if(ranks.getDonorRankId() != null){
            playerDisplayName = TextConversions.parse(ranks.getDonorRank().getPrefix() + " <reset>" + player.getName());
            Team team = scoreboard.getTeam(donorRank.getId());
            if(team != null)
                team.addPlayer(player);
        }


        //Replace on chat event
        //Use scoreboard for player name prefix
        player.displayName(playerDisplayName);
        player.playerListName(playerDisplayName);

        attachments.put(player.getUniqueId(), newAttachment);

        for (String perm : ranks.getAllPermissions()) {
            newAttachment.setPermission(perm, true);
        }

        player.updateCommands();

        new LunarNametag().createPlayerNametag(player, ranks);
    }

    public void removePermissions(Player player) {
        PermissionAttachment attachment = attachments.remove(player.getUniqueId());
        if (attachment != null) {
            try {
                player.removeAttachment(attachment);
            } catch (IllegalArgumentException ignored) { }
        }
    }

}
