package com.pwing.pwingeco.skript.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import org.bukkit.event.Event;
import com.pwing.pwingeco.events.BalanceChangeEvent;

public class EvtBalanceChange extends SkriptEvent {

    @Override
    public boolean check(Event event) {
        return event instanceof BalanceChangeEvent;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "balance change event";
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        return true;
    }
}
