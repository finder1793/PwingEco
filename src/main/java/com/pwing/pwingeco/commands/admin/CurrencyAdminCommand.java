package com.pwing.pwingeco.commands.admin;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.commands.BaseCommand;
import com.pwing.pwingeco.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyAdminCommand extends BaseCommand {

    public CurrencyAdminCommand(PwingEco plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admin.pwing.currency")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin create <name> <symbol> [primary]");
                    return true;
                }
                createCurrency(sender, args);
                break;
            case "delete":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin delete <name>");
                    return true;
                }
                deleteCurrency(sender, args[1]);
                break;
            case "setitem":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /currencyadmin setitem <currency>");
                    return true;
                }
                setCurrencyItem(sender, args[1]);
                break;
            default:
                sendHelp(sender);
                break;
        }
        return true;
    }

    private void createCurrency(CommandSender sender, String[] args) {
        String name = args[1];
        String symbol = args[2];
        boolean primary = args.length > 3 && Boolean.parseBoolean(args[3]);
        
        Currency currency = new Currency(name, symbol, primary, null);
        plugin.getCurrencyManager().registerCurrency(currency);
        plugin.getCurrencyConfiguration().saveCurrency(currency);
        
        sender.sendMessage(ChatColor.GREEN + "Currency " + name + " created successfully!");
    }

    private void deleteCurrency(CommandSender sender, String name) {
        plugin.getCurrencyManager().removeCurrency(name);
        plugin.getConfig().set("currencies." + name.toLowerCase(), null);
        plugin.saveConfig();
        
        sender.sendMessage(ChatColor.GREEN + "Currency " + name + " deleted successfully!");
    }

    private void setCurrencyItem(CommandSender sender, String currencyName) {
        Player player = (Player) sender;
        ItemStack hand = player.getInventory().getItemInMainHand();
        
        if (hand == null || hand.getType().isAir()) {
            sender.sendMessage(ChatColor.RED + "You must hold an item in your main hand!");
            return;
        }

        plugin.getCurrencyManager().getCurrency(currencyName).ifPresentOrElse(
            currency -> {
                Currency updatedCurrency = new Currency(
                    currency.getName(),
                    currency.getSymbol(),
                    currency.isPrimary(),
                    hand.clone()
                );
                plugin.getCurrencyManager().registerCurrency(updatedCurrency);
                plugin.getCurrencyConfiguration().saveCurrency(updatedCurrency);
                sender.sendMessage(ChatColor.GREEN + "Currency item updated successfully!");
            },
            () -> sender.sendMessage(ChatColor.RED + "Currency not found!")
        );
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Currency Admin Commands ===");
        sender.sendMessage(ChatColor.YELLOW + "/currencyadmin create <name> <symbol> [primary] - Create a new currency");
        sender.sendMessage(ChatColor.YELLOW + "/currencyadmin delete <name> - Delete a currency");
        sender.sendMessage(ChatColor.YELLOW + "/currencyadmin setitem <currency> - Set currency item to held item");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "delete", "setitem");
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("setitem"))) {
            return plugin.getCurrencyManager().getAllCurrencies().stream()
                .map(Currency::getName)
                .collect(Collectors.toList());
        }
        return null;
    }
}
