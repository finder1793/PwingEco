package com.pwing.pwingeco.manager;

import com.pwing.pwingeco.currency.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Collection;

public class CurrencyManager {
    private final Map<String, Currency> currencies = new HashMap<>();
    private Currency primaryCurrency;
    
    public void registerCurrency(Currency currency) {
        currencies.put(currency.getName().toLowerCase(), currency);
        if (currency.isPrimary()) {
            primaryCurrency = currency;
        }
    }
    
    public void removeCurrency(String name) {
        Currency removed = currencies.remove(name.toLowerCase());
        if (removed != null && removed.isPrimary()) {
            primaryCurrency = null;
        }
    }
    
    public Optional<Currency> getCurrency(String name) {
        return Optional.ofNullable(currencies.get(name.toLowerCase()));
    }
    
    public Currency getPrimaryCurrency() {
        return primaryCurrency;
    }

    public Collection<Currency> getAllCurrencies() {
        return currencies.values();
    }
}
