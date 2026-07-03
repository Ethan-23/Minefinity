package org.evasive.me.minefinity.playerdata.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.evasive.me.minefinity.playerdata.component.ComponentType;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponentRegistry;
import org.evasive.me.minefinity.playerdata.database.DatabaseManager;
import org.evasive.me.minefinity.playerdata.model.PlayerData;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

public class PlayerDataRepository {

    private static final Type MAP_STR_INT = new TypeToken<Map<String, Integer>>() {}.getType();
    private static final Type MAP_STR_STR = new TypeToken<Map<String, String>>() {}.getType();

    // Fixed scalar columns; feature component columns are appended from the registry.
    private static final List<String> SCALAR_COLUMNS = List.of(
            "uuid", "username", "balance", "unlocked_block_tiers", "selected_block_tiers", "backpack_storage");

    private final DatabaseManager dbManager;
    private final PlayerDataComponentRegistry componentRegistry;
    private final Gson gson = new Gson();

    public PlayerDataRepository(DatabaseManager dbManager, PlayerDataComponentRegistry componentRegistry) {
        this.dbManager = dbManager;
        this.componentRegistry = componentRegistry;
    }

    /**
     * Load a player by UUID from the database.
     */
    public Optional<PlayerData> loadPlayer(UUID uuid) {
        String sql = "SELECT * FROM minefinity.player_data WHERE uuid = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                Map<String, Integer> unlockedBlockTiers = gson.fromJson(rs.getString("unlocked_block_tiers"), MAP_STR_INT);
                Map<String, String> selectedBlockTiers = gson.fromJson(rs.getString("selected_block_tiers"), MAP_STR_STR);
                Map<String, Integer> backpackStorage = gson.fromJson(rs.getString("backpack_storage"), MAP_STR_INT);

                PlayerData player = new PlayerData(
                        uuid,
                        rs.getString("username"),
                        rs.getDouble("balance"),
                        unlockedBlockTiers,
                        selectedBlockTiers,
                        backpackStorage
                );

                // Each feature's slice is deserialized generically via its registered type.
                for (ComponentType<?> type : componentRegistry.all()) {
                    player.set(readComponent(rs, type));
                }

                return Optional.of(player);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private <T extends PlayerDataComponent> T readComponent(ResultSet rs, ComponentType<T> type) throws SQLException {
        String json = rs.getString(type.id());
        T value = (json == null) ? null : gson.fromJson(json, type.type());
        return value != null ? value : type.defaultFactory().get();
    }

    /**
     * Save or update a single player. Columns are the fixed scalars plus one per registered component.
     */
    public void savePlayer(PlayerData player) {
        List<ComponentType<?>> components = new ArrayList<>(componentRegistry.all());

        List<String> columns = new ArrayList<>(SCALAR_COLUMNS);
        for (ComponentType<?> type : components) columns.add(type.id());

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(buildUpsertSql(columns))) {

            int i = 1;
            stmt.setString(i++, player.getUuid().toString());
            stmt.setString(i++, player.getUsername());
            stmt.setDouble(i++, player.getBalance());
            stmt.setString(i++, gson.toJson(player.getUnlockedBlockTiers()));
            stmt.setString(i++, gson.toJson(player.getSelectedBlockTiers()));
            stmt.setString(i++, gson.toJson(player.getBackpackStorage()));
            for (ComponentType<?> type : components) {
                stmt.setString(i++, gson.toJson(player.get(type.type())));
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String buildUpsertSql(List<String> columns) {
        StringJoiner cols = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");
        StringJoiner updates = new StringJoiner(", ");
        for (String column : columns) {
            cols.add(column);
            placeholders.add("?");
            if (!column.equals("uuid")) updates.add(column + " = VALUES(" + column + ")");
        }
        return "INSERT INTO minefinity.player_data (" + cols + ") VALUES (" + placeholders + ") "
                + "ON DUPLICATE KEY UPDATE " + updates;
    }
}
