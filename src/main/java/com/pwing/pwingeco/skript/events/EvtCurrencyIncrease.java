package com.pwing.pwingeco.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import org.bukkit.event.Event;
import com.pwing.pwingeco.events.CurrencyIncreaseEvent;

public class EvtCurrencyIncrease extends SkriptEvent {
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return event instanceof CurrencyIncreaseEvent;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "currency increase";
    }
}
