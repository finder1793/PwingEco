package com.pwing.pwingeco.config;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyConfiguration {
    private final PwingEco plugin;

    public CurrencyConfiguration(PwingEco plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadCurrencies();
    }

    public void loadCurrencies() {
        ConfigurationSection currenciesSection = plugin.getConfig().getConfigurationSection("currencies");
        if (currenciesSection == null) return;

        for (String key : currenciesSection.getKeys(false)) {
            ConfigurationSection currencySection = currenciesSection.getConfigurationSection(key);
            if (currencySection == null) continue;

            String name = currencySection.getString("name", key);
            String symbol = currencySection.getString("symbol", "$");
            boolean primary = currencySection.getBoolean("primary", false);
            
            // Create item representation
            ConfigurationSection itemSection = currencySection.getConfigurationSection("item-representation");
            ItemStack itemStack = createItemRepresentation(itemSection);

            Currency currency = new Currency(name, symbol, primary, itemStack, true, new ArrayList<>());
            plugin.getCurrencyManager().registerCurrency(currency);
        }
    }

    private ItemStack createItemRepresentation(ConfigurationSection section) {
        if (section == null) return new ItemStack(Material.PAPER);

        Material material = Material.valueOf(section.getString("material", "PAPER"));
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            String displayName = section.getString("display-name");
            if (displayName != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            }

            List<String> lore = section.getStringList("lore");
            if (!lore.isEmpty()) {
                meta.setLore(lore.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList()));
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }

    public void saveCurrency(Currency currency) {
        String path = "currencies." + currency.getName().toLowerCase();
        plugin.getConfig().set(path + ".name", currency.getName());
        plugin.getConfig().set(path + ".symbol", currency.getSymbol());
        plugin.getConfig().set(path + ".primary", currency.isPrimary());
        
        ItemStack item = currency.getItemRepresentation();
        plugin.getConfig().set(path + ".item-representation.material", item.getType().name());
        
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                plugin.getConfig().set(path + ".item-representation.display-name", 
                    meta.getDisplayName().replace(ChatColor.COLOR_CHAR, '&'));
            }
            if (meta.hasLore()) {
                List<String> coloredLore = meta.getLore().stream()
                    .map(line -> line.replace(ChatColor.COLOR_CHAR, '&'))
                    .collect(Collectors.toList());
                plugin.getConfig().set(path + ".item-representation.lore", coloredLore);
            }
        }
        
        plugin.saveConfig();
    }
}
