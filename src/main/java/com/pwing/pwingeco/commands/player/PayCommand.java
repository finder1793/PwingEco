package com.pwing.pwingeco.commands.player;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.commands.BaseCommand;
import com.pwing.pwingeco.currency.Currency;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand extends BaseCommand {

    public PayCommand(PwingEco plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /pay <player> <amount> <currency>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid amount!");
            return true;
        }

        String currencyName = args[2];
        plugin.getCurrencyManager().getCurrency(currencyName).ifPresentOrElse(
            currency -> processPayment(player, target, amount, currency),
            () -> player.sendMessage(ChatColor.RED + "Currency not found!")
        );

        return true;
    }

    private void processPayment(Player sender, Player target, double amount, Currency currency) {
        if (!plugin.getEconomyManager().has(sender.getUniqueId(), currency, amount)) {
            sender.sendMessage(ChatColor.RED + "You don't have enough " + currency.getName() + "!");
            return;
        }

        plugin.getEconomyManager().withdrawPlayer(sender.getUniqueId(), currency, amount);
        plugin.getEconomyManager().depositPlayer(target.getUniqueId(), currency, amount);

        sender.sendMessage(ChatColor.GREEN + "You sent " + currency.getSymbol() + amount + 
                          " to " + target.getName());
        target.sendMessage(ChatColor.GREEN + "You received " + currency.getSymbol() + amount + 
                          " from " + sender.getName());
    }
}
