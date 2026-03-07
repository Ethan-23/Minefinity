package org.evasive.me.minefinity.database;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.repository.PlayerRepository;
import org.evasive.me.minefinity.ranks.PlayerRanks;
import org.evasive.me.minefinity.ranks.RankManager;

import java.util.List;
import java.util.UUID;

public class RankDataHandler {

    private RankManager rankManager;
    private PlayerRepository playerRepository;

    public RankDataHandler(RankManager rankManager, PlayerRepository playerRepository) {
        this.rankManager = rankManager;
        this.playerRepository = playerRepository;
    }

    public void loadRanks(){
        try {
            List<UUID> uuids = playerRepository.getAllPlayerUUIDs(Minefinity.getRankDatabaseManager().getConnection());

            for (UUID uuid : uuids) {
                PlayerRanks playerRanks = playerRepository.loadPlayerRanks(uuid);
                rankManager.addPlayer(uuid, playerRanks);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(UUID uuid) {
        try {
            Minefinity.getCore().rankDatabaseConnect();
            playerRepository.saveRanks(uuid);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("FAILED TO SAVE");
            e.printStackTrace();
        }
        Minefinity.getRankDatabaseManager().closePool();
    }
}
