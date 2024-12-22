package com.pwing.pwingeco.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.skript.expressions.ExprBalance;
import com.pwing.pwingeco.skript.effects.EffectGiveMoney;
import com.pwing.pwingeco.skript.effects.EffectTakeMoney;
import com.pwing.pwingeco.skript.conditions.ConditionHasMoney;

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
        EffectTakeMoney.setPwingEco(pwingEco);
        ConditionHasMoney.setPwingEco(pwingEco);
        
        Skript.registerEffect(EffectGiveMoney.class, "(give|add) %number% %string% to %player%");
        Skript.registerEffect(EffectTakeMoney.class, "(take|remove) %number% %string% from %player%");
        Skript.registerCondition(ConditionHasMoney.class, "[pwingeco] %player% (has|have) [at least] %number% [of] [pwingeco] %string%");
        Skript.registerExpression(ExprBalance.class, Number.class, ExpressionType.PROPERTY);
    }
    
    private boolean isSkriptInstalled() {
        return pwingEco.getServer().getPluginManager().getPlugin("Skript") != null;
    }
}
