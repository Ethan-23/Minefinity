package org.evasive.me.minefinity.playerdata.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.playerdata.model.PlayerRanks;
import org.evasive.me.minefinity.playerdata.ranks.service.PermissionService;
import org.evasive.me.minefinity.playerdata.repository.PlayerRankRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RankService {

    private final PlayerRankRepository rankRepository;
    private final PermissionService permissionService;
    // Cache for online players
    private final Map<UUID, PlayerRanks> rankCache = new ConcurrentHashMap<>();

    public RankService(PlayerRankRepository rankRepository, PermissionService permissionService) {
        this.rankRepository = rankRepository;
        this.permissionService = permissionService;
    }

    /**
     * Load ranks from database and cache them
     */
    public PlayerRanks loadRanks(UUID uuid) {

        PlayerRanks ranks = rankRepository
                .loadRanks(uuid)
                .orElse(new PlayerRanks(null, null));

        rankCache.put(uuid, ranks);
        return ranks;
    }

    public void loadRanksAsync(UUID uuid, Consumer<PlayerRanks> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Minefinity.getCore(), () -> {
            PlayerRanks ranks = loadRanks(uuid);
            Bukkit.getScheduler().runTask(Minefinity.getCore(), () -> callback.accept(ranks));
        });
    }

    /**
     * Get cached ranks
     */
    public PlayerRanks getRanks(UUID uuid) {
        return rankCache.get(uuid);
    }

    /**
     * Save ranks immediately
     */
    public void saveRanks(UUID uuid) {

        PlayerRanks ranks = rankCache.get(uuid);
        if (ranks == null) return;

        rankRepository.saveRanks(uuid, ranks);
    }

    /**
     * Set donor rank
     */
    public void setDonorRank(UUID uuid, String donorRankId) {

        PlayerRanks ranks = rankCache.get(uuid);
        if (ranks == null) return;

        ranks.setDonorRankId(donorRankId);
        rankRepository.saveRanks(uuid, ranks);

        Player player = Bukkit.getOfflinePlayer(uuid).getPlayer();
        if(player == null) return;
        permissionService.applyPermissions(player, loadRanks(uuid));
    }

    /**
     * Set staff rank
     */
    public void setStaffRank(UUID uuid, String staffRankId) {

        PlayerRanks ranks = rankCache.get(uuid);
        if (ranks == null) return;

        ranks.setStaffRankId(staffRankId);
        rankRepository.saveRanks(uuid, ranks);

        Player player = Bukkit.getOfflinePlayer(uuid).getPlayer();
        if(player == null) return;
        permissionService.applyPermissions(player, loadRanks(uuid));

    }

    /**
     * Remove donor rank
     */
    public void removeDonorRank(UUID uuid) {

        PlayerRanks ranks = rankCache.get(uuid);
        if (ranks == null) return;

        ranks.setDonorRankId(null);
        rankRepository.saveRanks(uuid, ranks);
    }

    /**
     * Remove staff rank
     */
    public void removeStaffRank(UUID uuid) {

        PlayerRanks ranks = rankCache.get(uuid);
        if (ranks == null) return;

        ranks.setStaffRankId(null);
        rankRepository.saveRanks(uuid, ranks);
    }

    /**
     * Remove player from cache (on quit)
     */
    public void unloadRanks(UUID uuid) {
        rankCache.remove(uuid);
    }

    /**
     * Delete ranks from DB
     */
    public void deleteRanks(UUID uuid) {
        rankRepository.deleteRanks(uuid);
        rankCache.remove(uuid);
    }

}