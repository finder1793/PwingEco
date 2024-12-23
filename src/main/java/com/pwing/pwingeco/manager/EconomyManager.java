package com.pwing.pwingeco.manager;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.currency.Currency;
import com.pwing.pwingeco.storage.MySQLStorage;
import com.pwing.pwingeco.storage.YAMLStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

public class EconomyManager {
    private final PwingEco plugin;
    private final Map<String, Map<UUID, Double>> balances = new HashMap<>();
    private final YAMLStorage yamlStorage;
    private final MySQLStorage mysqlStorage;
    private final boolean useMySQL;

    public EconomyManager(PwingEco plugin) {
        this.plugin = plugin;
        this.useMySQL = plugin.getConfig().getString("storage.type", "YAML").equalsIgnoreCase("MYSQL");
        this.yamlStorage = useMySQL ? null : new YAMLStorage(plugin);
        this.mysqlStorage = useMySQL ? new MySQLStorage(plugin) : null;
        
        Bukkit.getOnlinePlayers().forEach(this::loadPlayerData);
    }

    public void loadPlayerData(Player player) {
        Map<String, Double> playerBalances = useMySQL 
            ? mysqlStorage.loadPlayerData(player.getUniqueId())
            : yamlStorage.loadPlayerData(player.getUniqueId());
            
        playerBalances.forEach((currency, balance) -> 
            balances.computeIfAbsent(currency, k -> new HashMap<>())
                    .put(player.getUniqueId(), balance));
    }

    public void savePlayerData(UUID uuid) {
        Map<String, Double> playerBalances = new HashMap<>();
        balances.forEach((currency, balanceMap) -> {
            Double balance = balanceMap.get(uuid);
            if (balance != null) {
                playerBalances.put(currency, balance);
            }
        });

        if (useMySQL) {
            mysqlStorage.savePlayerData(uuid, playerBalances);
        } else {
            yamlStorage.savePlayerData(uuid, playerBalances);
        }
    }

    public double getBalance(UUID player, Currency currency) {
        return balances
            .computeIfAbsent(currency.getName(), k -> new HashMap<>())
            .getOrDefault(player, 0.0);
    }

    public void setBalance(UUID player, Currency currency, double amount) {
        balances
            .computeIfAbsent(currency.getName(), k -> new HashMap<>())
            .put(player, amount);
    }

    public boolean has(UUID player, Currency currency, double amount) {
        return getBalance(player, currency) >= amount;
    }

    public void withdrawPlayer(UUID player, Currency currency, double amount) {
        setBalance(player, currency, getBalance(player, currency) - amount);
    }

    public void depositPlayer(UUID player, Currency currency, double amount) {
        setBalance(player, currency, getBalance(player, currency) + amount);
    }

    public Map<UUID, Double> getAllBalances(Currency currency) {
        return balances.getOrDefault(currency.getName(), new HashMap<>());
    }


    public boolean withdrawBalance(UUID playerUUID, String currencyName, double amount) {
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(currencyName);
        if (!currencyOpt.isPresent() || !has(playerUUID, currencyOpt.get(), amount)) {
            return false;
        }
        
        withdrawPlayer(playerUUID, currencyOpt.get(), amount);
        savePlayerData(playerUUID);
        return true;
    }

    public boolean depositBalance(UUID playerUUID, String currencyName, double amount) {
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(currencyName);
        if (!currencyOpt.isPresent() || amount < 0) {
            return false;
        }
        
        depositPlayer(playerUUID, currencyOpt.get(), amount);
        savePlayerData(playerUUID);
        return true;
    }

    public double getBalance(UUID playerUUID, String currencyName) {
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(currencyName);
        return currencyOpt.map(currency -> getBalance(playerUUID, currency))
                          .orElse(0.0);
    }
}