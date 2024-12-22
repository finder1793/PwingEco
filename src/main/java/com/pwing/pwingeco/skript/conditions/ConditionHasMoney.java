package com.pwing.pwingeco.skript.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import com.pwing.pwingeco.PwingEco;

public class ConditionHasMoney extends Condition {
    private static PwingEco pwingEco;
    
    public static void setPwingEco(PwingEco plugin) {
        pwingEco = plugin;
    }
    
    private Expression<Number> amount;
    private Expression<String> currency;
    private Expression<Player> player;

    @Override
    public boolean check(Event event) {
        Number amountValue = amount.getSingle(event);
        String currencyName = currency.getSingle(event);
        Player targetPlayer = player.getSingle(event);
        
        if (amountValue != null && currencyName != null && targetPlayer != null) {
            return pwingEco.getCurrencyManager().getCurrency(currencyName)
                .map(curr -> pwingEco.getEconomyManager().has(targetPlayer.getUniqueId(), curr, amountValue.doubleValue()))
                .orElse(false);
        }
        return false;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "has money condition";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        amount = (Expression<Number>) exprs[0];
        currency = (Expression<String>) exprs[1];
        player = (Expression<Player>) exprs[2];
        return true;
    }
}
