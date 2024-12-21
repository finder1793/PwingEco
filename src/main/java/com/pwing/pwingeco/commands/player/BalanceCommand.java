package com.pwing.pwingeco.commands.player;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.commands.BaseCommand;
import com.pwing.pwingeco.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends BaseCommand {
    
    public BalanceCommand(PwingEco plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        // Show all balances if no currency specified
        if (args.length == 0) {
            showAllBalances(player);
            return true;
        }

        // Show specific currency balance
        String currencyName = args[0];
        plugin.getCurrencyManager().getCurrency(currencyName).ifPresentOrElse(
            currency -> showBalance(player, currency),
            () -> player.sendMessage(ChatColor.RED + "Currency not found!")
        );

        return true;
    }

    private void showAllBalances(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Your Balances ===");
        for (Currency currency : plugin.getCurrencyManager().getAllCurrencies()) {
            showBalance(player, currency);
        }
    }

    private void showBalance(Player player, Currency currency) {
        double balance = plugin.getEconomyManager().getBalance(player.getUniqueId(), currency);
        player.sendMessage(ChatColor.YELLOW + currency.getName() + ": " + 
                          ChatColor.GREEN + currency.getSymbol() + balance);
    }
}
