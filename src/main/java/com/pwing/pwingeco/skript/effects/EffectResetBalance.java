package com.pwing.pwingeco.skript.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import com.pwing.pwingeco.PwingEco;

public class EffectResetBalance extends Effect {
    private static PwingEco pwingEco;
    
    public static void setPwingEco(PwingEco plugin) {
        pwingEco = plugin;
    }
    
    private Expression<Player> player;
    private Expression<String> currency;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        String curr = currency.getSingle(event);
        if (p != null && curr != null) {
            pwingEco.getEconomyManager().setBalance(p.getUniqueId(), 
                pwingEco.getCurrencyManager().getCurrency(curr).get(), 0);
        }
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "reset balance effect";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        currency = (Expression<String>) exprs[1];
        return true;
    }
}
