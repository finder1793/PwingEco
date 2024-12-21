package com.pwing.pwingeco.commands.player;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.commands.BaseCommand;
import com.pwing.pwingeco.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.UUID;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class BalanceTopCommand extends BaseCommand {

    public BalanceTopCommand(PwingEco plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Currency currency;
        if (args.length == 0) {
            currency = plugin.getCurrencyManager().getPrimaryCurrency();
        } else {
            currency = plugin.getCurrencyManager().getCurrency(args[0])
                .orElse(plugin.getCurrencyManager().getPrimaryCurrency());
        }

        showTopBalances(sender, currency);
        return true;
    }

    private void showTopBalances(CommandSender sender, Currency currency) {
        Map<UUID, Double> balances = plugin.getEconomyManager().getAllBalances(currency);
        
        // Sort and limit to top 10
        Map<UUID, Double> topTen = balances.entrySet().stream()
            .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));

        sender.sendMessage(ChatColor.GOLD + "=== Top 10 " + currency.getName() + " Balances ===");
        int rank = 1;
        for (Map.Entry<UUID, Double> entry : topTen.entrySet()) {
            String playerName = plugin.getServer().getOfflinePlayer(entry.getKey()).getName();
            sender.sendMessage(ChatColor.YELLOW + "#" + rank + ": " + playerName + " - " + 
                             ChatColor.GREEN + currency.getSymbol() + entry.getValue());
            rank++;
        }
    }
}
