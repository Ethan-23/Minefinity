package org.evasive.me.minefinity.database.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.miner.AutoMiner;
import org.evasive.me.minefinity.customItems.backpack.BackpackStorage;
import org.evasive.me.minefinity.player.BlockMilestone;
import org.evasive.me.minefinity.forge.service.ForgeMap;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.smelter.Smelter;
import org.evasive.me.minefinity.town.Town;
import org.evasive.me.minefinity.workshop.Engineer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Level;

public class PlayerSaveRepository {

    public String serializePlayerData(MinefinityPlayer player) {
        Gson gson = new GsonBuilder().create();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("backpack", player.getBackpackStorage());
        dataMap.put("forge", player.getForgeItems());
        dataMap.put("autoMiner", player.getAutoMiner());
        dataMap.put("engineer", player.getEngineer());
        dataMap.put("milestones", player.getBlockMilestones());
        dataMap.put("town", player.getTown());
        dataMap.put("smelter", player.getSmelter());

        return gson.toJson(dataMap);
    }

    public void save(MinefinityPlayer playerData) {
        String sql = """
            INSERT INTO players (uuid, username, block_tier, selected_block_tier, balance, data)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                username = VALUES(username),
                block_tier = VALUES(block_tier),
                selected_block_tier = VALUES(selected_block_tier),
                balance = VALUES(balance),
                data = VALUES(data);
        """;

        try (PreparedStatement stmt = Minefinity.getDatabaseManager()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, playerData.getUuid().toString());
            stmt.setString(2, playerData.getUsername());
            stmt.setInt(3, playerData.getBlockTier().ordinal());
            stmt.setInt(4, playerData.getSelectedBlockTier());
            stmt.setDouble(5, playerData.getBalance());
            stmt.setString(6, serializePlayerData(playerData));

            stmt.executeUpdate();

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().log(Level.SEVERE, "Failed to save player: " + playerData.getUsername(), e);
        }
    }

    public MinefinityPlayer load(UUID uuid) {
        String sql = "SELECT * FROM players WHERE uuid = ?";

        try (PreparedStatement stmt = Minefinity.getDatabaseManager().getConnection().prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null; // Player not found
            }

            BlockType blockType = BlockType.values()[rs.getInt("block_tier")];
            int selectedBlockTier = rs.getInt("selected_block_tier");
            double balance = rs.getDouble("balance");
            MinefinityPlayer player = new MinefinityPlayer(uuid, blockType, selectedBlockTier, balance);

            String jsonData = rs.getString("data");
            if (jsonData != null && !jsonData.isEmpty()) {
                Gson gson = new GsonBuilder().create();
                JsonObject obj = gson.fromJson(jsonData, JsonObject.class);

                player.setBackpackStorage(loadOrDefault(gson, obj, "backpack", BackpackStorage.class, BackpackStorage::new));
                player.setForgeItems(loadOrDefault(gson,obj, "forge", ForgeMap.class, ForgeMap::new));
                player.setAutoMiner(loadOrDefault(gson,obj, "autoMiner", AutoMiner.class, AutoMiner::new));
                player.setEngineer(loadOrDefault(gson,obj, "engineer", Engineer.class, Engineer::new));
                player.setBlockMilestones(loadOrDefault(gson,obj, "milestones", BlockMilestone.class, BlockMilestone::new));
                player.setTown(loadOrDefault(gson,obj, "town", Town.class, Town::new));
                player.setSmelter(loadOrDefault(gson,obj, "smelter", Smelter.class, Smelter::new));
            }

            return player;

        } catch (SQLException e) {
            Minefinity.getCore().getLogger().severe("Failed to load player " + uuid.toString());
            e.printStackTrace();
            return null;
        }
    }

    private <T> T loadOrDefault(Gson gson, JsonObject obj, String key, Class<T> clazz, Supplier<T> defaultSupplier) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            return defaultSupplier.get();
        }

        return gson.fromJson(obj.get(key), clazz);
    }

}
