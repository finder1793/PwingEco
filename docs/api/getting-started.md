# Getting Started with PwingEco API

## Adding PwingEco to Your Project

### Maven
```xml
<repository>
    <id>pwing-repo</id>
    <url>https://repo.pwing.net/repository/maven-public/</url>
</repository>

<dependency>
    <groupId>com.pwing</groupId>
    <artifactId>pwingeco</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>

getting-started.md
Gradle

repositories {
    maven { url 'https://repo.pwing.net/repository/maven-public/' }
}

dependencies {
    compileOnly 'com.pwing:pwingeco:1.0.0'
}

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


## Skript Integration

PwingEco provides native Skript support for easy scripting. Available syntaxes:

# skript
## Get player balance

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