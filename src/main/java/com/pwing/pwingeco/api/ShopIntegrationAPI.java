package com.pwing.pwingeco.api;


import com.pwing.pwingeco.PwingEco;
import org.bukkit.entity.Player;
import java.util.UUID;

public class ShopIntegrationAPI {
    private final PwingEco plugin;
    
    public ShopIntegrationAPI(PwingEco plugin) {
        this.plugin = plugin;
    }
    
    public boolean processPurchase(Player buyer, String currencyName, double amount) {
        // Handle purchase transaction
        return plugin.getEconomyManager().withdrawBalance(buyer.getUniqueId(), currencyName, amount);
    }
    
    public boolean processSale(Player seller, String currencyName, double amount) {
        // Handle sale transaction
        return plugin.getEconomyManager().depositBalance(seller.getUniqueId(), currencyName, amount);
    }
    
    public double getCurrencyBalance(UUID playerUUID, String currencyName) {
        return plugin.getEconomyManager().getBalance(playerUUID, currencyName);
    }
}
