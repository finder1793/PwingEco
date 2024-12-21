# Physical Currencies

## Overview
Physical currencies are represented by actual items in the game. Players can hold, trade, and store these items in their inventory.

## Setting Up Physical Currency
1. Create your currency first:
```bash
/currencyadmin create <name> <symbol> <primary>

physical.md

    Hold the desired item in your main hand
    Set it as the currency's physical form:

/currencyadmin edit <name>

Configuration

Configure physical properties in config.yml:

currencies:
  goldcoin:
    name: "Gold Coin"
    symbol: "◎"
    item-representation:
      material: GOLD_NUGGET
      display-name: "&6Gold Coin"
      lore:
        - "&7Official trading currency"
        - "&7Worth: &61 Gold Coin"

Features

    Items can be traded directly between players
    Physical items work with chest shops
    Automatic stacking for convenience
    Custom item names and lore
    Protected against item duplication
    Supports custom textures/models

Trading

Players can:

    Drop physical currency items
    Store them in chests
    Trade with other players directly
    Use them in chest shops
