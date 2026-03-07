package org.evasive.me.minefinity.ranks;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionService {

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
        if(ranks.getStaffRank() != null){
            playerDisplayName = TextConversions.parse(ranks.getStaffRank().getPrefix() + " <reset>" + player.getName());
        }else if(ranks.getDonorRank() != null){
            playerDisplayName = TextConversions.parse(ranks.getDonorRank().getPrefix() + " <reset>" + player.getName());
        }


        //Replace on chat event
        //Use scoreboard for player name prefix
        //player.displayName(playerDisplayName);
        player.playerListName(playerDisplayName);
        attachments.put(player.getUniqueId(), newAttachment);

        for (String perm : ranks.getAllPermissions()) {
            newAttachment.setPermission(perm, true);
        }

        player.updateCommands();
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
