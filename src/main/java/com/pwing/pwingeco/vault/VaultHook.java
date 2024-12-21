package com.pwing.pwingeco.vault;

import com.pwing.pwingeco.PwingEco;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {
    private final PwingEco plugin;
    private PwingEcoVaultEconomy vaultEconomy;
    
    public VaultHook(PwingEco plugin) {
        this.plugin = plugin;
    }
    
    public void hook() {
        vaultEconomy = new PwingEcoVaultEconomy(plugin);
        plugin.getServer().getServicesManager().register(
            Economy.class,
            vaultEconomy,
            plugin,
            ServicePriority.Highest
        );
    }
    
    public void unhook() {
        plugin.getServer().getServicesManager().unregister(Economy.class, vaultEconomy);
    }
}
