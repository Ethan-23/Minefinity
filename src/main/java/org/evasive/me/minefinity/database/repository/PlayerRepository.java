package org.evasive.me.minefinity.database.repository;

import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.ranks.PlayerRanks;
import org.evasive.me.minefinity.ranks.RankManager;
import org.evasive.me.minefinity.ranks.RankRegistry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerRepository {

    private final PlayerManager playerManager;
    private final PlayerSaveRepository playerSaveRepository;
    private final PlayerRankSaveRepository playerRankSaveRepository;
    private final RankManager rankManager;

    public PlayerRepository(PlayerManager playerManager, RankRegistry rankRegistry, RankManager rankManager) {
        this.playerManager = playerManager;
        this.rankManager = rankManager;
        this.playerRankSaveRepository = new PlayerRankSaveRepository(rankRegistry);
        this.playerSaveRepository = new PlayerSaveRepository();
    }

    public void save(UUID uuid) {
        playerSaveRepository.save(playerManager.get(uuid));
    }

    public void saveRanks(UUID uuid) {
        playerRankSaveRepository.save(uuid, rankManager.getRanks(uuid));
    }

    public MinefinityPlayer loadMinefinityPlayer(UUID uuid) {

        return playerSaveRepository.load(uuid);
    }

    public PlayerRanks loadPlayerRanks(UUID uuid){
        return playerRankSaveRepository.load(uuid);
    }

    public List<UUID> getAllPlayerUUIDs(Connection connection) {
        List<UUID> uuids = new ArrayList<>();

        String sql = "SELECT uuid FROM players";

        try (Connection conn = connection;
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String uuidString = rs.getString("uuid");

                if (uuidString == null || uuidString.isEmpty()) {
                    continue;
                }

                try {
                    uuids.add(UUID.fromString(uuidString));
                } catch (IllegalArgumentException ex) {
                    Minefinity.getCore().getLogger().warning("Invalid UUID in database: " + uuidString);
                }
            }

        } catch (Exception e) {
            Minefinity.getCore().getLogger().severe("Failed to load players from database");
            e.printStackTrace();
        }

        return uuids;
    }

}
