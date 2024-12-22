package com.pwing.pwingeco.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.pwing.pwingeco.currency.Currency;

public class CurrencyDecreaseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Currency currency;
    private final double amount;
    private final double newBalance;

    public CurrencyDecreaseEvent(Player player, Currency currency, double amount, double newBalance) {
        this.player = player;
        this.currency = currency;
        this.amount = amount;
        this.newBalance = newBalance;
    }

    public Player getPlayer() { return player; }
    public Currency getCurrency() { return currency; }
    public double getAmount() { return amount; }
    public double getNewBalance() { return newBalance; }

    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}
