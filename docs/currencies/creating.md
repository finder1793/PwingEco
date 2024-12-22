# Creating Currencies

## Using Commands
Create currencies in-game using the admin command:

```
/currencyadmin create <name> <symbol> <primary>
```


Example:

```
/currencyadmin create Gems ⛃ false
```

Physical Currency Setup

    Create the currency using the command above
    Hold the item you want to represent the currency
    Use /currencyadmin edit <name> to set the item

Configuration File

You can also create currencies directly in the config.yml:

```
currencies:
  gems:
    name: "Gems"
    symbol: "⛃"
    primary: false
    item-representation:
      material: EMERALD
      display-name: "&aGem"
      lore:
        - "&7A precious currency"
    global: true
    enabled-worlds: []
    tradeable: true
    description:
      - "Premium currency"
      - "Used for special items"


```
Currency Properties

    name: Display name shown in messages
    symbol: Currency symbol for formatting
    primary: Set to true for main server currency
    global: Whether currency works in all worlds
    enabled-worlds: Specific worlds where currency is valid
    tradeable: Whether players can exchange this currency
    description: Lore/description of the currency
