package org.evasive.me.minefinity.database.repository;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.CustomItemRegistry;
import org.evasive.me.minefinity.database.DatabaseManager;
import org.evasive.me.minefinity.forge.data.ForgeItem;
import org.evasive.me.minefinity.player.MinefinityPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.ItemFunctions.getItemId;

public class ForgeRepository {

    public void save(MinefinityPlayer player) {

        Map<Integer, ForgeItem> forgeItems = player.getForgeItems();

        try (Connection conn = Minefinity.getDatabaseManager().getConnection()) {

            try (PreparedStatement delete = conn.prepareStatement(
                    "DELETE FROM forge WHERE uuid = ?")) {
                delete.setString(1, player.getUuid().toString());
                delete.executeUpdate();
            }

            String sql = """
                INSERT INTO forge (uuid, slot, time_finished, item_id)
                VALUES (?, ?, ?, ?)
            """;

            if(forgeItems == null || forgeItems.isEmpty()) return;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                for (Map.Entry<Integer, ForgeItem> entry : forgeItems.entrySet()) {

                    int slot = entry.getKey();
                    ForgeItem item = entry.getValue();

                    stmt.setString(1, player.getUuid().toString());
                    stmt.setInt(2, slot);
                    stmt.setLong(3, item.getTimeFinished());
                    stmt.setString(4, getItemId(item.getItemStack()));

                    stmt.addBatch();
                }

                stmt.executeBatch();
            }

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to save forge");
        }
    }

    public void load(MinefinityPlayer player) {


        String sql = "SELECT slot, time_finished, item_id FROM forge WHERE uuid = ?";

        try (PreparedStatement stmt = Minefinity.getDatabaseManager().getConnection().prepareStatement(sql)) {

            stmt.setString(1, player.getUuid().toString());
            ResultSet rs = stmt.executeQuery();

            Map<Integer, ForgeItem> forgeItems = player.getForgeItems();
            forgeItems.clear();

            while (rs.next()) {
                int slot = rs.getInt("slot");
                long timeFinished = rs.getLong("time_finished");
                String itemId = rs.getString("item_id");

                ForgeItem item = new ForgeItem(timeFinished, CustomItemRegistry.getByID(itemId).getBuilder().buildItem());
                forgeItems.put(slot, item);
            }

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().info("Failed to load forge");
        }
    }

}
