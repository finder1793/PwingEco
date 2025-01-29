package com.pwing.pwingeco.skript.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import com.pwing.pwingeco.PwingEco;

public class ConditionExactBalance extends Condition {
    private static PwingEco pwingEco;
    
    public static void setPwingEco(PwingEco plugin) {
        pwingEco = plugin;
    }
    
    private Expression<Player> player;
    private Expression<String> currency;
    private Expression<Number> balance;

    @Override
    public boolean check(Event event) {
        Player p = player.getSingle(event);
        String curr = currency.getSingle(event);
        Number bal = balance.getSingle(event);
        if (p != null && curr != null && bal != null) {
            return pwingEco.getEconomyManager().getBalance(p.getUniqueId(), 
                pwingEco.getCurrencyManager().getCurrency(curr).get()) == bal.doubleValue();
        }
        return false;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "exact balance condition";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        currency = (Expression<String>) exprs[1];
        balance = (Expression<Number>) exprs[2];
        return true;
    }
}
