package com.pwing.pwingeco.skript.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import com.pwing.pwingeco.PwingEco;

public class ExprBalance extends SimpleExpression<Number> {
    private static PwingEco pwingEco;
    
    public static void setPwingEco(PwingEco plugin) {
        pwingEco = plugin;
    }
    
    private Expression<Player> player;
    private Expression<String> currency;

    @Override
    protected Number[] get(Event event) {
        Player p = player.getSingle(event);
        String curr = currency.getSingle(event);
        if (p != null && curr != null) {
            return new Number[]{pwingEco.getEconomyManager().getBalance(p.getUniqueId(), 
                pwingEco.getCurrencyManager().getCurrency(curr).get())};
        }
        return new Number[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "balance expression";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        currency = (Expression<String>) exprs[1];
        return true;
    }
}