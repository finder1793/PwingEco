package com.pwing.pwingeco;

import com.pwing.pwingeco.config.CurrencyConfiguration;
import com.pwing.pwingeco.manager.CurrencyManager;
import com.pwing.pwingeco.manager.EconomyManager;
import com.pwing.pwingeco.commands.admin.CurrencyAdminCommand;
import com.pwing.pwingeco.commands.player.BalanceCommand;
import com.pwing.pwingeco.commands.player.BalanceTopCommand;
import com.pwing.pwingeco.commands.player.PayCommand;
import com.pwing.pwingeco.placeholder.EconomyPlaceholders;
import com.pwing.pwingeco.vault.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;
import com.pwing.pwingeco.listeners.PlayerDataListener;

public class PwingEco extends JavaPlugin {
    private CurrencyManager currencyManager;
    private CurrencyConfiguration currencyConfiguration;
    private VaultHook vaultHook;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        this.currencyManager = new CurrencyManager();
        this.currencyConfiguration = new CurrencyConfiguration(this);
        this.economyManager = new EconomyManager(this);

        
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            this.vaultHook = new VaultHook(this);
            this.vaultHook.hook();
            getLogger().info("Vault integration enabled!");
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new EconomyPlaceholders(this).register();
            getLogger().info("PlaceholderAPI integration enabled!");
        }

        getCommand("currencyadmin").setExecutor(new CurrencyAdminCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("balancetop").setExecutor(new BalanceTopCommand(this));
        getLogger().info("PwingEco has been enabled!");
        getServer().getPluginManager().registerEvents(new PlayerDataListener(this), this);
    }

    @Override
    public void onDisable() {
        if (vaultHook != null) {
            vaultHook.unhook();
        }
        getLogger().info("PwingEco has been disabled!");
    }
    
    public CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    public CurrencyConfiguration getCurrencyConfiguration() {
        return currencyConfiguration;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}
