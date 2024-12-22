package com.pwing.pwingeco.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.config.CurrencyConfiguration;

public class ExprBalance extends SimpleExpression<Number> {
    
    private static PwingEco pwingEco;
    
    static {
        Skript.registerExpression(ExprBalance.class, Number.class, ExpressionType.PROPERTY,
                "[pwingeco] balance of %player% [in %string%]",
                "%player%'s [pwingeco] balance [in %string%]");
    }
    
    private Expression<Player> player;
    private Expression<String> currency;

    public static void setPwingEco(PwingEco plugin) {
        pwingEco = plugin;
    }
    
    @Override
    public Number[] get(Event e) {
        Player p = player.getSingle(e);
        String curr = currency != null ? currency.getSingle(e) : null;
        
        if (p == null) return null;
        
        var currencyObj = curr != null ? 
            pwingEco.getCurrencyManager().getCurrency(curr).orElse(pwingEco.getCurrencyManager().getPrimaryCurrency()) :
            pwingEco.getCurrencyManager().getPrimaryCurrency();
            
        return new Number[] {pwingEco.getEconomyManager().getBalance(p.getUniqueId(), currencyObj)};
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
    public String toString(Event e, boolean debug) {
        return "balance of " + player.toString(e, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        currency = exprs.length > 1 ? (Expression<String>) exprs[1] : null;
        return true;
    }
}
