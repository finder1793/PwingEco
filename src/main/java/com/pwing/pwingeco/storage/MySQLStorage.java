package com.pwing.pwingeco.storage;

import com.pwing.pwingeco.PwingEco;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySQLStorage {
    private final PwingEco plugin;
    private HikariDataSource dataSource;

    public MySQLStorage(PwingEco plugin) {
        this.plugin = plugin;
        setupDatabase();
    }

    private void setupDatabase() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + plugin.getConfig().getString("storage.mysql.host") + ":" 
            + plugin.getConfig().getInt("storage.mysql.port") + "/" 
            + plugin.getConfig().getString("storage.mysql.database"));
        config.setUsername(plugin.getConfig().getString("storage.mysql.username"));
        config.setPassword(plugin.getConfig().getString("storage.mysql.password"));
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS pwingeco_balances ("
                + "uuid VARCHAR(36),"
                + "currency VARCHAR(50),"
                + "balance DOUBLE,"
                + "PRIMARY KEY (uuid, currency))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerData(UUID uuid, Map<String, Double> balances) {
        String sql = "INSERT INTO pwingeco_balances (uuid, currency, balance) VALUES (?, ?, ?) "
            + "ON DUPLICATE KEY UPDATE balance = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Map.Entry<String, Double> entry : balances.entrySet()) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, entry.getKey());
                stmt.setDouble(3, entry.getValue());
                stmt.setDouble(4, entry.getValue());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Double> loadPlayerData(UUID uuid) {
        Map<String, Double> balances = new HashMap<>();
        String sql = "SELECT currency, balance FROM pwingeco_balances WHERE uuid = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                balances.put(rs.getString("currency"), rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balances;
    }
}
