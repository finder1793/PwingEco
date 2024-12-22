package com.pwing.pwingeco.skript.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import com.pwing.pwingeco.PwingEco;

public class EffectGiveMoney extends Effect {
    private static PwingEco pwingEco;
    
    public static void setPwingEco(PwingEco plugin) {
        pwingEco = plugin;
    }
    
    private Expression<Number> amount;
    private Expression<String> currency;
    private Expression<Player> player;

    @Override
    protected void execute(Event event) {
        Number amountValue = amount.getSingle(event);
        String currencyName = currency.getSingle(event);
        Player targetPlayer = player.getSingle(event);
        
        if (amountValue != null && currencyName != null && targetPlayer != null) {
            pwingEco.getCurrencyManager().getCurrency(currencyName).ifPresent(curr -> 
                pwingEco.getEconomyManager().depositPlayer(targetPlayer.getUniqueId(), curr, amountValue.doubleValue())
            );
        }
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "give money effect";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        amount = (Expression<Number>) exprs[0];
        currency = (Expression<String>) exprs[1];
        player = (Expression<Player>) exprs[2];
        return true;
    }
}