package org.evasive.me.minefinity.database;

import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.repository.PlayerRepository;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.player.PlayerManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ServerDataHandler {

    private final PlayerManager playerManager;
    private final PlayerRepository playerRepository;
    private final DirtyPlayerService dirtyPlayerService;

    public ServerDataHandler(PlayerManager playerManager, PlayerRepository playerRepository, DirtyPlayerService dirtyPlayerService) {
        this.playerManager = playerManager;
        this.playerRepository = playerRepository;
        this.dirtyPlayerService = dirtyPlayerService;
    }

    // Load all players into memory
    public void loadAll() throws SQLException {
        try {
            List<UUID> uuids = playerRepository.getAllPlayerUUIDs(Minefinity.getDatabaseManager().getConnection());

            for (UUID uuid : uuids) {
                MinefinityPlayer player = playerRepository.loadMinefinityPlayer(uuid);
                playerManager.registerPlayer(uuid, player);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean haveDirty(){
        return !dirtyPlayerService.getDirtyPlayers().isEmpty();
    }

    public void saveDirty(){
        Set<UUID> dirtyPlayers = dirtyPlayerService.getDirtyPlayers();

        if(dirtyPlayers.isEmpty()) return;

        for(UUID uuid : dirtyPlayers){
            playerRepository.save(uuid);
        }
        dirtyPlayerService.clearDirtyPlayers();
    }

}
