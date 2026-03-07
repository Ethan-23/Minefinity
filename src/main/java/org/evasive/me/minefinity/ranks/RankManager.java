package org.evasive.me.minefinity.ranks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RankManager {

    Map<UUID, PlayerRanks> ranks = new HashMap<>();
    private final PermissionService permissionService;

    public RankManager(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public void addPlayer(UUID uuid){
        if(!containsPlayer(uuid) || getDonorRank(uuid) == null){
            //put default update to put default rank
            ranks.put(uuid, new PlayerRanks(null, null));
        }
    }

    public void addPlayer(UUID uuid, PlayerRanks playerRanks){
        if(!containsPlayer(uuid) || getDonorRank(uuid) == null){
            //put default update to put default rank
            ranks.put(uuid, playerRanks);
        }
    }

    public boolean containsPlayer(UUID uuid){
        return ranks.containsKey(uuid);
    }

    public void setRank(Player player, Rank rank){

        if(rank.getCategory() == RankCategory.STAFF){
            setStaffRank(player.getUniqueId(), rank);
        }else {
            setDonorRank(player.getUniqueId(), rank);
        }

        Minefinity.getCore().getRankDataHandler().save(player.getUniqueId());

        permissionService.applyPermissions(player, ranks.get(player.getUniqueId()));

    }

    public void removeRank(Player player, Rank rank){
        if(rank.getCategory() == RankCategory.STAFF){
            getRanks(player.getUniqueId()).setStaffRank(null);
        }else  {
            getRanks(player.getUniqueId()).setDonorRank(null);
        }

        permissionService.applyPermissions(player, ranks.get(player.getUniqueId()));

    }

    public PlayerRanks getRanks(UUID uuid){
        return ranks.get(uuid);
    }

    public void setDonorRank(UUID uuid, Rank rank) {
        if(rank.getCategory() != RankCategory.DONOR)
            return;
        ranks.get(uuid).setDonorRank(rank);
    }

    public void setStaffRank(UUID uuid, Rank rank) {
        if(rank.getCategory() != RankCategory.STAFF)
            return;
        ranks.get(uuid).setStaffRank(rank);
    }

    public Rank getStaffRank(UUID uuid){
        return ranks.get(uuid).getStaffRank();
    }

    public Rank getDonorRank(UUID uuid){
        return ranks.get(uuid).getDonorRank();
    }



}
