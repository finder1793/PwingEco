package com.pwing.pwingeco.placeholder;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.currency.Currency;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class EconomyPlaceholders extends PlaceholderExpansion {
    private final PwingEco plugin;

    public EconomyPlaceholders(PwingEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "pwingeco";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";

        // Format: balance_<currency>
        if (identifier.startsWith("balance_")) {
            String currencyName = identifier.substring(8);
            return plugin.getCurrencyManager().getCurrency(currencyName)
                .map(currency -> String.format("%.2f", plugin.getEconomyManager().getBalance(player.getUniqueId(), currency)))
                .orElse("0.00");
        }

        // Format: symbol_<currency>
        if (identifier.startsWith("symbol_")) {
            String currencyName = identifier.substring(7);
            return plugin.getCurrencyManager().getCurrency(currencyName)
                .map(Currency::getSymbol)
                .orElse("");
        }

        return null;
    }
}
