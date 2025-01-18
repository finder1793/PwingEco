package com.pwing.pwingeco.api;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.currency.Currency;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class ShopIntegrationAPI {
    private final PwingEco plugin;
    
    public ShopIntegrationAPI(PwingEco plugin) {
        this.plugin = plugin;
    }
    
    public boolean processPurchase(Player buyer, String currencyName, double amount) {
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(currencyName);
        if (!currencyOpt.isPresent()) {
            return false;
        }
        Currency currency = currencyOpt.get();
        if (currency.isItemBased()) {
            return plugin.getEconomyManager().withdrawItems(buyer, currency, (int) amount);
        } else {
            return plugin.getEconomyManager().withdrawBalance(buyer.getUniqueId(), currencyName, amount);
        }
    }
    
    public boolean processSale(Player seller, String currencyName, double amount) {
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(currencyName);
        if (!currencyOpt.isPresent()) {
            return false;
        }
        Currency currency = currencyOpt.get();
        if (currency.isItemBased()) {
            return plugin.getEconomyManager().depositItems(seller, currency, (int) amount);
        } else {
            return plugin.getEconomyManager().depositBalance(seller.getUniqueId(), currencyName, amount);
        }
    }
    
    public double getCurrencyBalance(UUID playerUUID, String currencyName) {
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(currencyName);
        if (!currencyOpt.isPresent()) {
            return 0.0;
        }
        Currency currency = currencyOpt.get();
        if (currency.isItemBased()) {
            // Calculate the total amount of items in the player's inventory
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) {
                return 0.0;
            }
            int totalAmount = 0;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.isSimilar(currency.getItemRepresentation())) {
                    totalAmount += item.getAmount();
                }
            }
            return totalAmount;
        } else {
            return plugin.getEconomyManager().getBalance(playerUUID, currencyName);
        }
    }
}
