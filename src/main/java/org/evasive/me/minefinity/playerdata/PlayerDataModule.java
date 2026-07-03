package org.evasive.me.minefinity.playerdata;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.playerdata.commands.StatCommand;
import org.evasive.me.minefinity.playerdata.commands.rank.MineRank;
import org.evasive.me.minefinity.playerdata.database.DatabaseManager;
import org.evasive.me.minefinity.playerdata.listener.PlayerChatListener;
import org.evasive.me.minefinity.playerdata.listener.PlayerJoinListener;
import org.evasive.me.minefinity.playerdata.listener.PlayerPreLoginListener;
import org.evasive.me.minefinity.playerdata.listener.PlayerQuitListener;
import org.evasive.me.minefinity.playerdata.ranks.RankRegistry;
import org.evasive.me.minefinity.playerdata.ranks.config.PermissionConfigManager;
import org.evasive.me.minefinity.playerdata.ranks.config.PermissionLoader;
import org.evasive.me.minefinity.playerdata.ranks.service.PermissionService;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponentRegistry;
import org.evasive.me.minefinity.playerdata.repository.PlayerDataRepository;
import org.evasive.me.minefinity.playerdata.repository.PlayerRankRepository;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.service.RankService;
import org.evasive.me.minefinity.playerdata.stats.events.StatsListeners;
import org.evasive.me.minefinity.playerdata.stats.StatContributorRegistry;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;

import static org.bukkit.Bukkit.getLogger;

public class PlayerDataModule {

    private final DatabaseManager playerDb;
    private final DatabaseManager rankDb;

    private final PlayerDataComponentRegistry componentRegistry;
    private final StatContributorRegistry statContributorRegistry;

    private final PlayerDataService playerService;
    private final RankService rankService;
    private final StatsService statsService;

    private final PermissionConfigManager permissionConfigManager;
    private final PermissionService permissionService;
    private final PermissionLoader permissionLoader;

    public PlayerDataModule() {

        playerDb = new DatabaseManager();
        rankDb = new DatabaseManager();

        componentRegistry = new PlayerDataComponentRegistry();
        PlayerDataRepository playerRepo = new PlayerDataRepository(playerDb, componentRegistry);
        PlayerRankRepository rankRepo = new PlayerRankRepository(rankDb);


        permissionConfigManager = new PermissionConfigManager();
        permissionConfigManager.createPermissionConfig();
        permissionLoader = new PermissionLoader(permissionConfigManager, RankRegistry.getInstance());

        playerService = new PlayerDataService(playerRepo, componentRegistry);
        permissionService = new PermissionService(Minefinity.getCore());
        rankService = new RankService(rankRepo, permissionService);
        statContributorRegistry = new StatContributorRegistry();
        statsService = new StatsService(playerService, statContributorRegistry);
    }

    public void enable(JavaPlugin plugin) {

        permissionLoader.loadRanks();

        ConfigurationSection databases = plugin.getConfig().getConfigurationSection("databases");

        if(databases == null)
            return;

        // Connect databases
        connectDatabase(playerDb, databases.getConfigurationSection("players"), "players");
        connectDatabase(rankDb, databases.getConfigurationSection("ranks"), "ranks");

        // Register listeners
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new PlayerPreLoginListener(playerService), plugin);
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

    public PlayerDataComponentRegistry getComponentRegistry() {
        return componentRegistry;
    }

    public RankService getRankService() {
        return rankService;
    }

    public StatsService getStatsService() {
        return statsService;
    }

    public StatContributorRegistry getStatContributorRegistry() {
        return statContributorRegistry;
    }

    private void connectDatabase(DatabaseManager databaseManager, ConfigurationSection databaseInfo, String label) {
        if (databaseManager == null) return;
        if (databaseInfo == null) {
            Minefinity.SendLogMessage("No config section for the '" + label + "' database. Skipping connection.");
            return;
        }

        String host = databaseInfo.getString("host");
        String database = databaseInfo.getString("database");
        String username = databaseInfo.getString("username");
        String password = databaseInfo.getString("password");

        if (host == null || database == null || username == null || password == null) {
            Minefinity.SendLogMessage("Incomplete '" + label + "' database config (host/database/username/password). Skipping connection.");
            return;
        }

        int port = databaseInfo.getInt("port", 3306); // default instead of silent 0

        try {
            databaseManager.connect(host, port, database, username, password);
            Minefinity.SendLogMessage("Connected to '" + label + "' database (" + database + ").");
        } catch (Exception e) {
            Minefinity.getCore().getLogger().log(java.util.logging.Level.SEVERE,
                    "Failed to connect to '" + label + "' database (" + database + ")", e);
        }
    }

}