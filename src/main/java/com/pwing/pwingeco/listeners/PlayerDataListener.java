package com.pwing.pwingeco.listeners;

import com.pwing.pwingeco.PwingEco;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {
    private final PwingEco plugin;

    public PlayerDataListener(PwingEco plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getEconomyManager().loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getEconomyManager().savePlayerData(event.getPlayer().getUniqueId());
    }
}
