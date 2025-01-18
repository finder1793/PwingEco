package com.pwing.pwingeco.commands.player;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.currency.Currency;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class WithdrawItemsCommand implements CommandExecutor {
    private final PwingEco plugin;

    public WithdrawItemsCommand(PwingEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage("Usage: /withdrawitems <amount> <currency>");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid amount.");
            return true;
        }
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(args[1]);
        if (!currencyOpt.isPresent() || !currencyOpt.get().isItemBased()) {
            player.sendMessage("Invalid or non-item-based currency.");
            return true;
        }
        Currency currency = currencyOpt.get();
        if (plugin.getEconomyManager().withdrawItems(player, currency, amount)) {
            player.sendMessage("Items withdrawn successfully.");
        } else {
            player.sendMessage("Failed to withdraw items.");
        }
        return true;
    }
}
