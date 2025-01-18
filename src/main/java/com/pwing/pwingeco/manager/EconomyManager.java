package com.pwing.pwingeco.manager;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.currency.Currency;
import com.pwing.pwingeco.storage.MySQLStorage;
import com.pwing.pwingeco.storage.YAMLStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public boolean depositItems(Player player, Currency currency, int amount) {
        if (!currency.isItemBased()) return false;
        ItemStack item = currency.getItemRepresentation();
        item.setAmount(amount);
        player.getInventory().addItem(item);
        return true;
    }

    public boolean withdrawItems(Player player, Currency currency, int amount) {
        if (!currency.isItemBased()) return false;
        ItemStack item = currency.getItemRepresentation();
        item.setAmount(amount);
        if (player.getInventory().containsAtLeast(item, amount)) {
            player.getInventory().removeItem(item);
            return true;
        }
        return false;
    }

    public void autoCombineItems(Player player, Currency currency) {
        if (!currency.isItemBased()) return;
        ItemStack item = currency.getItemRepresentation();
        int totalAmount = 0;
        for (ItemStack invItem : player.getInventory().getContents()) {
            if (invItem != null && invItem.isSimilar(item)) {
                totalAmount += invItem.getAmount();
                player.getInventory().remove(invItem);
            }
        }
        while (totalAmount > 0) {
            int stackSize = Math.min(totalAmount, currency.getMaxStackSize());
            item.setAmount(stackSize);
            player.getInventory().addItem(item);
            totalAmount -= stackSize;
        }
        combineToNextTier(player, currency, totalAmount);
    }

    private void combineToNextTier(Player player, Currency currency, int totalAmount) {
        if (currency.getNextTierItem() == null || currency.getCombineAmount() <= 0) return;
        int nextTierAmount = totalAmount / currency.getCombineAmount();
        int remainingAmount = totalAmount % currency.getCombineAmount();
        ItemStack nextTierItem = currency.getNextTierItem();
        while (nextTierAmount > 0) {
            int stackSize = Math.min(nextTierAmount, nextTierItem.getMaxStackSize());
            nextTierItem.setAmount(stackSize);
            player.getInventory().addItem(nextTierItem);
            nextTierAmount -= stackSize;
        }
        if (remainingAmount > 0) {
            ItemStack item = currency.getItemRepresentation();
            item.setAmount(remainingAmount);
            player.getInventory().addItem(item);
        }
    }
}