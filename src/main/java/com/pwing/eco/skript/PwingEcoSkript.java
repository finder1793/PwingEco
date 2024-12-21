package com.pwing.pwingeco.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.skript.expressions.ExprBalance;
import com.pwing.pwingeco.skript.effects.EffectGiveMoney;
import ch.njol.skript.lang.ExpressionType;

public class PwingEcoSkript {
    private final PwingEco pwingEco;
    
    public PwingEcoSkript(PwingEco pwingEco) {
        this.pwingEco = pwingEco;
    }
    
    public void register() {
        if (!isSkriptInstalled()) {
            return;
        }
        
        ExprBalance.setPwingEco(pwingEco);
        EffectGiveMoney.setPwingEco(pwingEco);
        
        Skript.registerEffect(EffectGiveMoney.class);
        Skript.registerExpression(ExprBalance.class, Number.class, ExpressionType.PROPERTY);
    }
    
    private boolean isSkriptInstalled() {
        return pwingEco.getServer().getPluginManager().getPlugin("Skript") != null;
    }
}