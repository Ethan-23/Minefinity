package org.evasive.me.minefinity.playerdata.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.evasive.me.minefinity.mining.milestones.BlockMilestone;
import org.evasive.me.minefinity.playerdata.database.PlayersDatabaseManager;
import org.evasive.me.minefinity.playerdata.model.*;
import org.evasive.me.minefinity.towns.data.TownData;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.BaseForgeItem;
import org.evasive.me.minefinity.towns.structures.forge.smelter.Smelter;
import org.evasive.me.minefinity.towns.structures.mines.miner.AutoMinerData;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.Engineer;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

public class PlayerDataRepository {

    private final PlayersDatabaseManager dbManager;
    private final Gson gson = new Gson();

    public PlayerDataRepository(PlayersDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Load a player by UUID from the database
     */
    public Optional<PlayerData> loadPlayer(UUID uuid) {
        String sql = "SELECT * FROM minefinity.player_data WHERE uuid = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                String username = rs.getString("username");
                double balance = rs.getDouble("balance");

                // Deserialize JSON fields
                Type unlockedBlockType = new TypeToken<Map<String, Integer>>() {}.getType();
                Map<String, Integer> unlockedBlockTiers = gson.fromJson(rs.getString("unlocked_block_tiers"), unlockedBlockType);
                Type selectedBlockType = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> selectedBlockTiers = gson.fromJson(rs.getString("selected_block_tiers"), selectedBlockType);
                TownData townData = gson.fromJson(rs.getString("town_data"), TownData.class);
                AutoMinerData autoMinerData = gson.fromJson(rs.getString("auto_miner_data"), AutoMinerData.class);
                BlockMilestone blockMilestone = gson.fromJson(rs.getString("milestone_data"), BlockMilestone.class);
                Engineer engineer =  gson.fromJson(rs.getString("engineer_data"), Engineer.class);
                Smelter smelter = gson.fromJson(rs.getString("smelter_data"), Smelter.class);
                Type forgeItemsType = new TypeToken<Map<Integer, BaseForgeItem>>() {}.getType();
                Map<Integer, BaseForgeItem> forgeItems = gson.fromJson(rs.getString("forge_items"), forgeItemsType);
                Type backpackType = new TypeToken<Map<String, Integer>>() {}.getType();
                Map<String, Integer> backpackStorage = gson.fromJson(rs.getString("backpack_storage"), backpackType);

                PlayerData player = new PlayerData(
                        uuid,
                        username,
                        unlockedBlockTiers,
                        selectedBlockTiers,
                        blockMilestone,
                        balance,
                        townData,
                        autoMinerData,
                        engineer,
                        smelter,
                        forgeItems,
                        backpackStorage
                );

                return Optional.of(player);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Save or update a single player
     */
    public void savePlayer(PlayerData player) {
        String sql = """
                INSERT INTO minefinity.player_data
                (uuid, username, balance, unlocked_block_tiers, selected_block_tiers, milestone_data, town_data, auto_miner_data, smelter_data, forge_items, engineer_data, backpack_storage)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                username = VALUES(username),
                balance = VALUES(balance),
                unlocked_block_tiers = VALUES(unlocked_block_tiers),
                selected_block_tiers = VALUES(selected_block_tiers),
                milestone_data = VALUES(milestone_data),
                town_data = VALUES(town_data),
                auto_miner_data = VALUES(auto_miner_data),
                smelter_data = VALUES(smelter_data),
                forge_items = VALUES(forge_items),
                engineer_data = VALUES(engineer_data),
                backpack_storage = VALUES(backpack_storage)
                """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUuid().toString());
            stmt.setString(2, player.getUsername());
            stmt.setDouble(3, player.getBalance());
            stmt.setString(4, gson.toJson(player.getUnlockedBlockTiers()));
            stmt.setString(5, gson.toJson(player.getSelectedBlockTiers()));

            stmt.setString(6, gson.toJson(player.getBlockMilestones()));
            stmt.setString(7, gson.toJson(player.getTownData()));
            stmt.setString(8, gson.toJson(player.getAutoMinerData()));
            stmt.setString(9, gson.toJson(player.getSmelter()));
            stmt.setString(10, gson.toJson(player.getForgeItems()));
            stmt.setString(11, gson.toJson(player.getEngineer()));
            stmt.setString(12, gson.toJson(player.getBackpackStorage()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}