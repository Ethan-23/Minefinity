package org.evasive.me.minefinity.database.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.backpack.BackpackStorage;
import org.evasive.me.minefinity.forge.service.ForgeMap;
import org.evasive.me.minefinity.miner.AutoMiner;
import org.evasive.me.minefinity.player.BlockMilestone;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.ranks.PlayerRanks;
import org.evasive.me.minefinity.ranks.Rank;
import org.evasive.me.minefinity.ranks.RankManager;
import org.evasive.me.minefinity.ranks.RankRegistry;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.smelter.Smelter;
import org.evasive.me.minefinity.town.Town;
import org.evasive.me.minefinity.workshop.Engineer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerRankSaveRepository {

    private final RankRegistry rankRegistry;

    public PlayerRankSaveRepository(RankRegistry rankRegistry) {
        this.rankRegistry = rankRegistry;
    }

    public void save(UUID uuid, PlayerRanks playerRanks) {
        String sql = """
            INSERT INTO players (uuid, username, donor_rank, staff_rank)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                username = VALUES(username),
                donor_rank = VALUES(donor_rank),
                staff_rank = VALUES(staff_rank)
        """;

        try (PreparedStatement stmt = Minefinity.getRankDatabaseManager()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            stmt.setString(2, Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid)).getName());
            stmt.setString(3, playerRanks.getDonorRank() == null ? null : playerRanks.getDonorRank().getId());
            stmt.setString(4, playerRanks.getStaffRank() == null ? null : playerRanks.getStaffRank().getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().log(Level.SEVERE, "Failed to save player: ", e);
        }
    }

    public PlayerRanks load(UUID uuid) {
        String sql = "SELECT * FROM players WHERE uuid = ?";

        try (PreparedStatement stmt = Minefinity.getRankDatabaseManager().getConnection().prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null; // Player not found
            }

            Rank donorRank = rankRegistry.getRank(rs.getString("donor_rank"));
            Rank staffRank = rankRegistry.getRank(rs.getString("staff_rank"));

            return new PlayerRanks(staffRank, donorRank);

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().severe("Failed to load player " + uuid.toString());
            e.printStackTrace();
            return null;
        }
    }


}
