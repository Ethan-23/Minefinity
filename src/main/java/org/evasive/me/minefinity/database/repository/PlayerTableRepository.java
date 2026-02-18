package org.evasive.me.minefinity.database.repository;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.DatabaseManager;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.resourceblock.BlockType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerTableRepository {

    public void save(MinefinityPlayer playerData) {
        String sql = """
            INSERT INTO players (uuid, block_tier, selected_block_tier, blocks_mined, balance)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                block_tier = VALUES(block_tier),
                selected_block_tier = VALUES(selected_block_tier),
                blocks_mined = VALUES(blocks_mined),
                balance = VALUES(balance)
        """;

        try (PreparedStatement stmt = Minefinity.getDatabaseManager()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, playerData.getUuid().toString());
            stmt.setInt(2, playerData.getBlockTier().ordinal());
            stmt.setInt(3, playerData.getSelectedBlockTier());
            stmt.setLong(4, playerData.getBlocksMined());
            stmt.setDouble(5, playerData.getBalance());

            stmt.executeUpdate();

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to save player");
        }
    }

    public MinefinityPlayer load(UUID uuid) {


        String sql = "SELECT * FROM players WHERE uuid = ?";

        try (PreparedStatement stmt = Minefinity.getDatabaseManager()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            return new MinefinityPlayer(
                    uuid,
                    BlockType.values()[rs.getInt("block_tier")],
                    rs.getInt("selected_block_tier"),
                    rs.getLong("blocks_mined"),
                    rs.getDouble("balance")
            );

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to load player");
        }

        return null;
    }

}
