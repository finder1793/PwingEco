package com.pwing.pwingeco.commands.admin;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.currency.Currency;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Optional;

public class CurrencyAdminCommand implements CommandExecutor {
    private final PwingEco plugin;

    public CurrencyAdminCommand(PwingEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin <create|edit|remove|give|take> <name> [args...]");
            return true;
        }

        String action = args[0].toLowerCase();
        switch (action) {
            case "create":
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin create <name> <symbol> <primary>");
                    return true;
                }
                handleCreate(sender, args[1], args[2], Boolean.parseBoolean(args[3]));
                break;
            case "edit":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can edit currencies!");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin edit <name>");
                    return true;
                }
                handleEdit(sender, args[1]);
                break;
            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin remove <name>");
                    return true;
                }
                handleRemove(sender, args[1]);
                break;
            case "give":
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin give <player> <amount> <currency>");
                    return true;
                }
                handleGive(sender, args[1], Double.parseDouble(args[2]), args[3]);
                break;
            case "take":
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin take <player> <amount> <currency>");
                    return true;
                }
                handleTake(sender, args[1], Double.parseDouble(args[2]), args[3]);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown action: " + action);
                break;
        }
        return true;
    }

    private void handleCreate(CommandSender sender, String name, String symbol, boolean primary) {
        Optional<Currency> existingCurrency = plugin.getCurrencyManager().getCurrency(name);
        if (existingCurrency.isPresent()) {
            sender.sendMessage(ChatColor.RED + "A currency with that name already exists!");
            return;
        }

        Currency currency = new Currency(name, symbol, primary, null, true, new ArrayList<>(), false, 0, null, 0);
        plugin.getCurrencyManager().registerCurrency(currency);
        plugin.getCurrencyConfiguration().saveCurrency(currency);
        sender.sendMessage(ChatColor.GREEN + "Created new currency: " + name);
    }

    private void handleEdit(CommandSender sender, String name) {
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(name);
        if (!currencyOpt.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Currency not found: " + name);
            return;
        }

        Player player = (Player) sender;
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand == null || hand.getType().isAir()) {
            sender.sendMessage(ChatColor.RED + "You must hold an item to set as currency representation!");
            return;
        }

        Currency currency = currencyOpt.get();
        Currency updatedCurrency = new Currency(
            currency.getName(),
            currency.getSymbol(),
            currency.isPrimary(),
            hand.clone(),
            true,
            new ArrayList<>(),
            false,
            0,
            null,
            0
        );

        plugin.getCurrencyManager().registerCurrency(updatedCurrency);
        plugin.getCurrencyConfiguration().saveCurrency(updatedCurrency);
        sender.sendMessage(ChatColor.GREEN + "Updated currency: " + name);
    }

    private void handleRemove(CommandSender sender, String name) {
        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(name);
        if (!currencyOpt.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Currency not found: " + name);
            return;
        }

        Currency currency = currencyOpt.get();
        plugin.getCurrencyManager().unregisterCurrency(currency);
        plugin.getConfig().set("currencies." + name.toLowerCase(), null);
        plugin.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Removed currency: " + name);
    }

    private void handleGive(CommandSender sender, String playerName, double amount, String currencyName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(currencyName);
        if (!currencyOpt.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Currency not found!");
            return;
        }

        plugin.getEconomyManager().depositPlayer(target.getUniqueId(), currencyOpt.get(), amount);
        sender.sendMessage(ChatColor.GREEN + "Gave " + amount + currencyOpt.get().getSymbol() + " to " + target.getName());
    }

    private void handleTake(CommandSender sender, String playerName, double amount, String currencyName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        Optional<Currency> currencyOpt = plugin.getCurrencyManager().getCurrency(currencyName);
        if (!currencyOpt.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Currency not found!");
            return;
        }

        plugin.getEconomyManager().withdrawPlayer(target.getUniqueId(), currencyOpt.get(), amount);
        sender.sendMessage(ChatColor.GREEN + "Took " + amount + currencyOpt.get().getSymbol() + " from " + target.getName());
    }
}
