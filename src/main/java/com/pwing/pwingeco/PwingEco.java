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
import com.pwing.pwingeco.skript.PwingEcoSkript;
import com.pwing.pwingeco.commands.admin.ReloadCommand;
import com.pwing.pwingeco.commands.admin.SaveCommand;
import com.pwing.pwingeco.commands.player.DepositItemsCommand;
import com.pwing.pwingeco.commands.player.WithdrawItemsCommand;
import java.io.File;
import java.io.IOException;

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

        loadConfiguration();
        
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            this.vaultHook = new VaultHook(this);
            this.vaultHook.hook();
            getLogger().info("Vault integration enabled!");
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new EconomyPlaceholders(this).register();
            getLogger().info("PlaceholderAPI integration enabled!");
        }
        if (getServer().getPluginManager().getPlugin("Skript") != null) {
            new PwingEcoSkript(this).register();
            getLogger().info("Skript integration enabled!");
        }
        registerCommands();
        getLogger().info("PwingEco has been enabled!");
        getServer().getPluginManager().registerEvents(new PlayerDataListener(this), this);

    }

    @Override
    public void onDisable() {
        if (vaultHook != null) {
            vaultHook.unhook();
        }
        saveConfiguration();
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

    private void loadConfiguration() {
        try {
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                getDataFolder().mkdirs();
                saveDefaultConfig();
            }
            getLogger().info("Configuration loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load configuration: " + e.getMessage());
        }
    }

    public void saveConfiguration() {
        try {
            saveConfig();
            getLogger().info("Configuration saved successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to save configuration: " + e.getMessage());
        }
    }

    private void registerCommands() {
        getCommand("currencyadmin").setExecutor(new CurrencyAdminCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("balancetop").setExecutor(new BalanceTopCommand(this));
        getCommand("pwingeco").setExecutor(new ReloadCommand(this));
        getCommand("pwingecosave").setExecutor(new SaveCommand(this));
        getCommand("deposititems").setExecutor(new DepositItemsCommand(this));
        getCommand("withdrawitems").setExecutor(new WithdrawItemsCommand(this));
    }

    public void reloadConfiguration() {
        try {
            reloadConfig();
            getLogger().info("Configuration reloaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to reload configuration: " + e.getMessage());
        }
    }
}
