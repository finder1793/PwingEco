package com.pwing.pwingeco.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.pwing.pwingeco.currency.Currency;

public class BalanceChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String currency;
    private final double oldBalance;
    private final double newBalance;

    public BalanceChangeEvent(Player player, String currency, double oldBalance, double newBalance) {
        this.player = player;
        this.currency = currency;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCurrency() {
        return currency;
    }

    public double getOldBalance() {
        return oldBalance;
    }

    public double getNewBalance() {
        return newBalance;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
