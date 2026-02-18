package org.evasive.me.minefinity.database.repository;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.DatabaseManager;
import org.evasive.me.minefinity.player.MinefinityPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerRepository {

    private final PlayerTableRepository playerTableRepo = new PlayerTableRepository();
    private final MilestoneRepository milestoneRepo = new MilestoneRepository();
    private final AutoMinerRepository autoMinerRepo = new AutoMinerRepository();
    private final BackpackRepository backpackRepo = new BackpackRepository();
    private final ForgeRepository forgeRepo = new ForgeRepository();
    private final TownRepository townRepo = new TownRepository();

    public void save(MinefinityPlayer player) {

        playerTableRepo.save(player);
        backpackRepo.save(player);
        milestoneRepo.save(player);
        autoMinerRepo.saveMiner(player);
        forgeRepo.save(player);
        townRepo.save(player);

    }

    public MinefinityPlayer load(UUID uuid) {

        MinefinityPlayer minefinityPlayer = playerTableRepo.load(uuid);
        milestoneRepo.loadInto(minefinityPlayer);
        autoMinerRepo.loadMiner(minefinityPlayer);
        backpackRepo.load(minefinityPlayer);
        forgeRepo.load(minefinityPlayer);
        townRepo.load(minefinityPlayer);

        return minefinityPlayer;
    }

    public List<UUID> getAllPlayerUUIDs() {
        List<UUID> uuids = new ArrayList<>();

        String sql = "SELECT uuid FROM players";

        try (Connection conn = Minefinity.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String uuidString = rs.getString("uuid");

                if (uuidString == null || uuidString.isEmpty()) {
                    continue;
                }

                try {
                    uuids.add(UUID.fromString(uuidString));
                } catch (IllegalArgumentException ex) {
                    Minefinity.getCore().getLogger().warning("Invalid UUID in database: " + uuidString);
                }
            }

        } catch (Exception e) { // NOT just SQLException
            Minefinity.getCore().getLogger().severe("Failed to load players from database");
            e.printStackTrace();
        }

        return uuids;
    }

}
