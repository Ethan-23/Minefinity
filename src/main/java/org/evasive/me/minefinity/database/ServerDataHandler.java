package org.evasive.me.minefinity.database;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.repository.PlayerRepository;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.player.PlayerManager;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ServerDataHandler {

    private final PlayerManager playerManager;
    private final PlayerRepository playerRepository;

    public ServerDataHandler(PlayerManager playerManager, PlayerRepository playerRepository) {
        this.playerManager = playerManager;
        this.playerRepository = playerRepository;
    }

    // Load all players into memory
    public void loadAll() throws SQLException {
        try {
            List<UUID> uuids = playerRepository.getAllPlayerUUIDs();

            for (UUID uuid : uuids) {
                //Bukkit.getConsoleSender().sendMessage("RUNNING FOR " + uuid.toString());
                MinefinityPlayer player = playerRepository.load(uuid);
                playerManager.registerPlayer(uuid, player);
                //Bukkit.getConsoleSender().sendMessage("DONE FOR " + uuid.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Save all players from memory to database
    public void saveAll() {
        try {
            playerManager.getAll().values().forEach(playerRepository::save);
        } finally {
            // Disconnect after all saves are done
        }
    }

}
