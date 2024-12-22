
# Configuration

## Main Configuration (config.yml)
```
storage:
  type: YAML  # YAML or MySQL
  mysql:
    host: localhost
    port: 3306
    database: pwingeco
    username: root
    password: password
    pool-size: 10
```
### Currency Settings
```yaml
currencies:
  dollars:
    name: "Dollars"
    symbol: "$"
    primary: true
    item-representation:
      material: PAPER
      display-name: "&aOne Dollar Bill"
      lore:
        - "&7Official currency"
    global: true
    enabled-worlds: []
    tradeable: true
    description:
      - "The primary currency"
      - "Used throughout the server"
```
