Here's the managing.md documentation for currencies:

# Managing Currencies

## Admin Commands
### Edit Currency
Modify a currency's physical representation:
```bash
/currencyadmin edit <name>

managing.md

Hold the item you want to use as the currency's physical form when running this command.
Remove Currency

Delete an existing currency:

/currencyadmin remove <name>

World Management

Currencies can be:

    Global (usable everywhere)
    World-specific (only in enabled worlds)

Configure in config.yml:

currencies:
  gems:
    global: false
    enabled-worlds:
      - "world"
      - "world_shop"

Trading System

Control whether currencies can be traded:

currencies:
  gems:
    tradeable: true

Primary Currency

    Only one currency can be primary
    Used as default when no currency specified
    Required for Vault integration

Set primary status:

currencies:
  dollars:
    primary: true
