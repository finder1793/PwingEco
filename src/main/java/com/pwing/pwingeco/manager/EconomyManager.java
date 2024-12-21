package com.pwing.pwingeco.manager;

import com.pwing.pwingeco.currency.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {
    private final Map<String, Map<UUID, Double>> balances = new HashMap<>();

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
}
