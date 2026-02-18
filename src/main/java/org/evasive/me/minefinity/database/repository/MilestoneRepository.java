package org.evasive.me.minefinity.database.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.resourceblock.ResourceData;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MilestoneRepository {

    private final Gson gson = new Gson();

    public String serializeMilestones(Map<BlockType, ResourceData> backpack) {
        return gson.toJson(backpack);
    }


    public void save(MinefinityPlayer playerData) {

        String sql = """
            INSERT INTO milestones (uuid, resource_data)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE
                resource_data = VALUES(resource_data)
        """;

        try (Connection conn = Minefinity.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, playerData.getUuid().toString());
            Map<BlockType, ResourceData> milestones = playerData.getBlockMilestones();
            if (milestones == null) milestones = new HashMap<>();
            stmt.setString(2, serializeMilestones(milestones));

            stmt.executeUpdate();

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to save milestones");
        }

    }

    public void loadInto(MinefinityPlayer player) {

        String sql = """
            SELECT resource_data
            FROM milestones
            WHERE uuid = ?
        """;

        try (Connection conn = Minefinity.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUuid().toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String json = rs.getString("resource_data");
                if (json == null || json.isEmpty()) {
                    player.setBlockMilestones(new HashMap<>());
                } else {
                    Type mapType = new TypeToken<Map<BlockType, ResourceData>>() {}.getType();
                    player.setBlockMilestones(gson.fromJson(json, mapType));
                }
            } else {
                player.setBlockMilestones(new HashMap<>());
            }

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to load milestones for " + player.getUuid());
        }
    }

}
