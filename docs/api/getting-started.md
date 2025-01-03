# Getting Started with PwingEco API

## Adding PwingEco to Your Project

### Maven
```
xml
<repository>
    <id>pwing-repo</id>
    <url>https://repo.pwing.net/repository/maven-public/</url>
</repository>

<dependency>
    <groupId>com.pwing</groupId>
    <artifactId>PwingEco</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>


Gradle

repositories {
    maven { url 'https://repo.pwing.net/repository/maven-public/' }
}

dependencies {
    compileOnly 'com.pwing:pwingeco:1.0.0'
}
```

Basic Usage
Getting the Plugin Instance

PwingEco pwingEco = (PwingEco) Bukkit.getPluginManager().getPlugin("PwingEco");

Currency Management

CurrencyManager currencyManager = pwingEco.getCurrencyManager();

// Get a currency
Optional<Currency> currency = currencyManager.getCurrency("dollars");

// Get primary currency
Currency primary = currencyManager.getPrimaryCurrency();

// Get all currencies
Collection<Currency> currencies = currencyManager.getAllCurrencies();

Creating Custom Currencies

Currency customCurrency = new Currency(
    "Gems",          // name
    "⛃",            // symbol
    false,          // primary
    itemStack,      // physical representation
    true,           // tradeable
    description     // list of descriptions
);

currencyManager.registerCurrency(customCurrency);

Economy Operations

EconomyManager economyManager = pwingEco.getEconomyManager();

// Check balance
double balance = economyManager.getBalance(player.getUniqueId(), currency);

// Add funds
economyManager.deposit(player.getUniqueId(), currency, amount);

// Remove funds
economyManager.withdraw(player.getUniqueId(), currency, amount);
## Shop Integration

### Getting the Shop API
```
ShopIntegrationAPI shopAPI = pwingEco.getShopIntegrationAPI();
```

Processing Transactions
```
// Handle a purchase
boolean purchaseSuccess = shopAPI.processPurchase(player, "Gems", 500.0);
if (purchaseSuccess) {
    // Purchase successful
}
```

```
// Handle a sale
boolean saleSuccess = shopAPI.processSale(player, "Coins", 100.0);
if (saleSuccess) {
    // Sale successful
}
```

Balance Operations

```
// Check if player can afford purchase
double balance = shopAPI.getCurrencyBalance(player.getUniqueId(), "Diamonds");
if (balance >= cost) {
    // Proceed with transaction
}
```

Transaction Best Practices
Currency Validation

Always verify currency existence before transactions:

```
Optional<Currency> currency = pwingEco.getCurrencyManager().getCurrency("Gems");
if (currency.isPresent()) {
    // Safe to proceed
}
```
Error Handling

```
try {
    boolean success = shopAPI.processPurchase(player, "Gems", amount);
    if (!success) {
        // Handle failed transaction
    }
} catch (Exception e) {
    // Handle unexpected errors
}
```
Multi-Currency Support

```
// Example handling multiple currencies in a shop
for (String currencyName : shop.getAcceptedCurrencies()) {
    double price = shop.getPrice(currencyName);
    if (shopAPI.getCurrencyBalance(player.getUniqueId(), currencyName) >= price) {
        // Process with this currency
        break;
    }
}
```

## Skript Integration

PwingEco provides native Skript support for easy scripting. Available syntaxes:

# Get player balance
```
[pwingeco] balance of %player% [in %string%]
%player%'s [pwingeco] balance [in %string%]
```
# Give/add money
```
give %number% [of] [pwingeco] %string% to %player%
add %number% [of] [pwingeco] %string% to %player%'s balance
```

## Take/remove money

```
take %number% [of] [pwingeco] %string% from %player%
remove %number% [of] [pwingeco] %string% from %player%'s balance
```

## Check if player has money

```
[pwingeco] %player% has [at least] %number% [of] [pwingeco] %string%
```

### Example:

```
    take 100 dollars from player
    remove 100 dollars from player
    player has 100 dollars
    pwingeco player has at least 50 gems
    player has 75 of pwingeco coins
    pwingeco player has at least 1000 of pwingeco diamonds
```

## Currency Events

PwingEco provides events for tracking currency changes:

### Skript Events
Track currency increases and decreases:
skript
# Currency gained
```
on player gained 100 of coins:
    broadcast "%player% received %number% %string%!"
```

# Currency spent
```
on pwingeco player spent 50 of gems:
    send "Purchase complete!"
```
Event Patterns

Currency Increase:

```
    [on] [pwingeco] %player% (gain[ed]|receiv(e|ed)) %number% [of] [pwingeco] %string%
```

Currency Decrease:
```
    [on] [pwingeco] %player% (lost|spent) %number% [of] [pwingeco] %string%
```
