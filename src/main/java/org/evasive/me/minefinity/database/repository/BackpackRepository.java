package org.evasive.me.minefinity.database.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.DatabaseManager;
import org.evasive.me.minefinity.player.MinefinityPlayer;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BackpackRepository {

    private final Gson gson = new Gson();

    public String serializeBackpack(Map<String, Integer> backpack) {
        return gson.toJson(backpack);
    }

    public void save(MinefinityPlayer player) {

        Map<String, Integer> backpack = player.getBackpackStorage();

        try (Connection conn = Minefinity.getDatabaseManager().getConnection()) {

            String sql = """
                INSERT INTO backpack (uuid, inventory)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE inventory = VALUES(inventory)
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, player.getUuid().toString());
                stmt.setString(2, serializeBackpack(backpack));

                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to save backpack");
        }
    }

    public void load(MinefinityPlayer player) {

        String sql = "SELECT inventory FROM backpack WHERE uuid = ?";

        try (Connection conn = Minefinity.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUuid().toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String json = rs.getString("inventory");
                Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
                player.setBackpackStorage(gson.fromJson(json, mapType));
            } else {
                player.setBackpackStorage(new HashMap<>());
            }

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to load backpack for " + player.getUuid());
        }
    }

}
