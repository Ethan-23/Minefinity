package org.evasive.me.minefinity.playerdata.repository;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.playerdata.database.RankDatabaseManager;
import org.evasive.me.minefinity.playerdata.model.PlayerRanks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class PlayerRankRepository {

    private final RankDatabaseManager dbManager;

    public PlayerRankRepository() {
        this.dbManager = RankDatabaseManager.getInstance();
    }

    /**
     * Load ranks for a player
     */
    public Optional<PlayerRanks> loadRanks(UUID uuid) {

        String sql = "SELECT staff_rank, donor_rank FROM minefinity_ranks.players WHERE uuid = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    return Optional.empty();
                }

                String staffRank = rs.getString("staff_rank");
                String donorRank = rs.getString("donor_rank");

                return Optional.of(new PlayerRanks(staffRank, donorRank));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Save ranks for a player
     */
    public void saveRanks(UUID uuid, PlayerRanks ranks) {

        String sql = """
                INSERT INTO minefinity_ranks.players (uuid, username, staff_rank, donor_rank)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    username = VALUES(username),
                    staff_rank = VALUES(staff_rank),
                    donor_rank = VALUES(donor_rank)
                """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            stmt.setString(2, Bukkit.getOfflinePlayer(uuid).getName());
            stmt.setString(3, ranks.getStaffRankId());
            stmt.setString(4, ranks.getDonorRankId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete ranks (optional)
     */
    public void deleteRanks(UUID uuid) {

        String sql = "DELETE FROM minefinity_ranks.players WHERE uuid = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}