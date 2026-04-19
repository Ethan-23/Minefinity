package org.evasive.me.minefinity.playerdata;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.playerdata.commands.StatCommand;
import org.evasive.me.minefinity.playerdata.commands.rank.MineRank;
import org.evasive.me.minefinity.playerdata.database.PlayersDatabaseManager;
import org.evasive.me.minefinity.playerdata.database.RankDatabaseManager;
import org.evasive.me.minefinity.playerdata.listener.PlayerChatListener;
import org.evasive.me.minefinity.playerdata.listener.PlayerJoinListener;
import org.evasive.me.minefinity.playerdata.listener.PlayerQuitListener;
import org.evasive.me.minefinity.playerdata.ranks.RankRegistry;
import org.evasive.me.minefinity.playerdata.ranks.config.PermissionConfigManager;
import org.evasive.me.minefinity.playerdata.ranks.config.PermissionLoader;
import org.evasive.me.minefinity.playerdata.ranks.service.PermissionService;
import org.evasive.me.minefinity.playerdata.repository.PlayerDataRepository;
import org.evasive.me.minefinity.playerdata.repository.PlayerRankRepository;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.service.RankService;
import org.evasive.me.minefinity.playerdata.stats.events.StatsListeners;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;

public class PlayerDataModule {

    private final PlayersDatabaseManager playerDb;
    private final RankDatabaseManager rankDb;

    private final PlayerDataService playerService;
    private final RankService rankService;
    private final StatsService statsService;

    private final PermissionConfigManager permissionConfigManager;
    private final PermissionService permissionService;
    private final PermissionLoader permissionLoader;

    public PlayerDataModule() {

        playerDb = PlayersDatabaseManager.getInstance();
        rankDb = RankDatabaseManager.getInstance();

        PlayerDataRepository playerRepo = new PlayerDataRepository(playerDb);
        PlayerRankRepository rankRepo = new PlayerRankRepository();


        permissionConfigManager = new PermissionConfigManager();
        permissionConfigManager.createPermissionConfig();
        permissionLoader = new PermissionLoader(permissionConfigManager, RankRegistry.getInstance());

        playerService = new PlayerDataService(playerRepo);
        permissionService = new PermissionService(Minefinity.getCore());
        rankService = new RankService(rankRepo, permissionService);
        statsService = new StatsService(playerService);

    }

    public void enable(JavaPlugin plugin) {

        permissionLoader.loadRanks();

        // Connect databases
        playerDb.connect("127.0.0.1", 3306, "minefinity", "admin", "jdf7tA@tf");
        rankDb.connect("127.0.0.1", 3306, "minefinity_ranks", "admin", "jdf7tA@tf");

        // Register listeners
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new PlayerJoinListener(playerService, rankService, permissionService), plugin);
        pm.registerEvents(new PlayerQuitListener(playerService, rankService, permissionService), plugin);
        pm.registerEvents(new PlayerChatListener(rankService), plugin);
        pm.registerEvents(new StatsListeners(statsService), plugin);
        // autosave task for player data
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                plugin,
                playerService::saveDirtyPlayers,
                20 * 60 * 5,
                20 * 60 * 5
        );

        //Commands
        new MineRank(rankService);
        new StatCommand(statsService);
    }

    public void disable() {

        // Save player data
        playerService.saveDirtyPlayersSync();
        playerService.shutdown();

        // Close pools
        playerDb.closePool();
        rankDb.closePool();
    }

    public PlayerDataService getPlayerService() {
        return playerService;
    }

    public RankService getRankService() {
        return rankService;
    }

    public StatsService getStatsService() {
        return statsService;
    }
}