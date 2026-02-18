package org.evasive.me.minefinity.database.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.automation.miner.data.AutoMiner;
import org.evasive.me.minefinity.database.DatabaseManager;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.resourceblock.BlockType;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AutoMinerRepository {

    private final Gson gson = new Gson();

    public String serializeInventory(Map<String, Integer> backpack) {
        return gson.toJson(backpack);
    }

    public void saveMiner(MinefinityPlayer player) {
        AutoMiner miner = player.getAutoMiner();
        Map<String, Integer> inventory = miner.getItemStorage();
        if (inventory == null) inventory = new HashMap<>();

        String sql = """
        INSERT INTO autominer (uuid, miner_id, pickaxe_data, block_type, level, inventory)
        VALUES (?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            pickaxe_data = VALUES(pickaxe_data),
            block_type = VALUES(block_type),
            level = VALUES(level),
            inventory = VALUES(inventory)
    """;

        try (Connection conn = Minefinity.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUuid().toString());
            stmt.setInt(2, 0); // hardcoded miner id
            stmt.setString(3, miner.getPickaxe() == null ? null : miner.serializePickaxe());
            stmt.setString(4, miner.getBlockType() == null ? null : miner.getBlockType().name());
            stmt.setInt(5, miner.getLevel());
            stmt.setString(6, gson.toJson(inventory));

            stmt.executeUpdate();

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().warning("Failed to save autominer for " + player.getUuid());
        }
    }

    public void loadMiner(MinefinityPlayer player) {
        String sql = """
        SELECT pickaxe_data, block_type, level, inventory
        FROM autominer
        WHERE uuid = ? AND miner_id = ?
    """;

        try (Connection conn = Minefinity.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUuid().toString());
            stmt.setInt(2, 0);

            ResultSet rs = stmt.executeQuery();

            AutoMiner miner = player.getAutoMiner();

            if (rs.next()) {
                miner.setPickaxe(miner.deserializePickaxe(rs.getString("pickaxe_data")));

                String block = rs.getString("block_type");
                miner.setBlockType(block == null ? null : BlockType.valueOf(block));

                miner.setLevel(rs.getInt("level"));

                String json = rs.getString("inventory");
                Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
                miner.setItemStorage(gson.fromJson(json, mapType));
            } else {
                miner.setItemStorage(new HashMap<>()); // default empty inventory
            }

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().warning("Failed to load autominer for " + player.getUuid());
            player.getAutoMiner().setItemStorage(new HashMap<>());
        }
    }

}
