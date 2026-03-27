package org.evasive.me.minefinity.playerdata.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class PlayersDatabaseManager {

    private static PlayersDatabaseManager instance;
    private HikariDataSource dataSource;

    private PlayersDatabaseManager() {}

    public static PlayersDatabaseManager getInstance() {
        if (instance == null) instance = new PlayersDatabaseManager();
        return instance;
    }

    public void connect(String host, int port, String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setKeepaliveTime(30000);
        config.setLeakDetectionThreshold(60000);
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closePool() {
        if (dataSource != null) dataSource.close();
    }
}