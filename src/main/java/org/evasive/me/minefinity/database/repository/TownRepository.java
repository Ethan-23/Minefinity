package org.evasive.me.minefinity.database.repository;

import com.google.gson.Gson;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.town.Town;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class TownRepository {

    private final Gson gson = new Gson();

    public String serializeTown(Town town) {
        return gson.toJson(town);
    }

    public Town deserializeTown(String town) {
        return gson.fromJson(town, Town.class);
    }

    public void save(MinefinityPlayer playerData) {
        String sql = """
            INSERT INTO towns (uuid, town)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE
                town = VALUES(town)
        """;

        try (PreparedStatement stmt = Minefinity.getDatabaseManager()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, playerData.getUuid().toString());
            stmt.setString(2, serializeTown(playerData.getTown()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to save Town");
        }
    }

    public void load(MinefinityPlayer player) {


        String sql = "SELECT * FROM towns WHERE uuid = ?";

        try (PreparedStatement stmt = Minefinity.getDatabaseManager()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, player.getUuid().toString());

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return;

            Town town = deserializeTown(rs.getString("town"));
            player.setTown(town);

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to load Town");
            e.printStackTrace();
        }

    }


}
