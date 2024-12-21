
# Commands

## Admin Commands
### /currencyadmin
Manage currencies in the system.

#### Usage
- `/currencyadmin create <name> <symbol> <primary>` - Create a new currency
- `/currencyadmin edit <name>` - Edit a currency's physical representation (hold item)
- `/currencyadmin remove <name>` - Remove a currency
- `/currencyadmin give <player> <amount> <currency>` - Give currency to a player
- `/currencyadmin take <player> <amount> <currency>` - Take currency from a player

#### Permissions
- `pwingeco.admin` - Access to all admin commands

## Player Commands
### /balance
View your currency balances.

#### Usage
- `/balance` - View all your currency balances
- `/balance <player>` - View another player's balances

### /pay
Transfer currency to other players.

#### Usage
- `/pay <player> <amount> [currency]` - Pay another player
- If currency is not specified, uses the primary currency

### /balancetop
View the richest players.

#### Usage
- `/balancetop [currency]` - View top balances
- If currency is not specified, uses the primary currency

#### Permissions
- `pwingeco.balance` - Access to balance command
- `pwingeco.balance.others` - View other players' balances
- `pwingeco.pay` - Access to pay command
- `pwingeco.balancetop` - Access to balancetop command
