# API Examples

## Currency Management Examples

### Creating a Custom Currency
```java
public void createCustomCurrency(PwingEco pwingEco) {
    // Create item representation
    ItemStack goldCoin = new ItemStack(Material.GOLD_NUGGET);
    ItemMeta meta = goldCoin.getItemMeta();
    meta.setDisplayName(ChatColor.GOLD + "Gold Coin");
    List<String> lore = Arrays.asList(
        ChatColor.GRAY + "Official trading currency",
        ChatColor.GRAY + "Value: " + ChatColor.GOLD + "1 Gold Coin"
    );
    meta.setLore(lore);
    goldCoin.setItemMeta(meta);

    // Create currency
    Currency currency = new Currency(
        "GoldCoin",
        "◎",
        false,
        goldCoin,
        true,
        Arrays.asList("Premium currency", "Used in special shops")
    );

    // Register currency
    pwingEco.getCurrencyManager().registerCurrency(currency);
}

examples.md
World-Specific Currency Handler

public class WorldCurrencyHandler implements Listener {
    private final PwingEco plugin;
    private final Currency currency;

    public WorldCurrencyHandler(PwingEco plugin, String currencyName) {
        this.plugin = plugin;
        this.currency = plugin.getCurrencyManager()
            .getCurrency(currencyName)
            .orElseThrow(() -> new IllegalStateException("Currency not found"));
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (currency.isEnabledInWorld(player.getWorld().getName())) {
            player.sendMessage("Currency " + currency.getName() + " is valid here!");
        }
    }
}

Balance Management System

public class BalanceManager {
    private final PwingEco plugin;

    public BalanceManager(PwingEco plugin) {
        this.plugin = plugin;
    }

    public void rewardPlayer(Player player, double amount) {
        Currency primary = plugin.getCurrencyManager().getPrimaryCurrency();
        plugin.getEconomyManager().deposit(player.getUniqueId(), primary, amount);
        player.sendMessage(String.format("You received %s%s!", primary.getSymbol(), amount));
    }

    public boolean purchaseItem(Player player, double cost) {
        Currency primary = plugin.getCurrencyManager().getPrimaryCurrency();
        if (plugin.getEconomyManager().has(player.getUniqueId(), primary, cost)) {
            plugin.getEconomyManager().withdraw(player.getUniqueId(), primary, cost);
            return true;
        }
        return false;
    }
}

Custom Shop Implementation

public class CustomShop implements Listener {
    private final PwingEco plugin;
    private final Currency shopCurrency;

    public CustomShop(PwingEco plugin, String currencyName) {
        this.plugin = plugin;
        this.shopCurrency = plugin.getCurrencyManager()
            .getCurrency(currencyName)
            .orElse(plugin.getCurrencyManager().getPrimaryCurrency());
    }

    @EventHandler
    public void onShopTransaction(InventoryClickEvent event) {
        // Shop logic implementation
        Player player = (Player) event.getWhoClicked();
        double price = 100.0; // Example price
        
        if (plugin.getEconomyManager().has(player.getUniqueId(), shopCurrency, price)) {
            plugin.getEconomyManager().withdraw(player.getUniqueId(), shopCurrency, price);
            // Give item to player
            player.sendMessage(String.format("Purchased for %s%s!", shopCurrency.getSymbol(), price));
        }
    }
}
//Skript integration
command /balance [<text>]:
    trigger:
        set {_bal} to player's pwingeco balance in arg-1
        message "Your balance: %{_bal}%"
