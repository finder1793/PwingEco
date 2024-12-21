package com.pwing.pwingeco.manager;

import com.pwing.pwingeco.currency.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Collection;
import java.util.Collections;

public class CurrencyManager {
    private final Map<String, Currency> currencies = new HashMap<>();

    public void registerCurrency(Currency currency) {
        currencies.put(currency.getName().toLowerCase(), currency);
    }

    public Optional<Currency> getCurrency(String name) {
        return Optional.ofNullable(currencies.get(name.toLowerCase()));
    }

    public void unregisterCurrency(Currency currency) {
        currencies.remove(currency.getName().toLowerCase());
    }

    public Currency getPrimaryCurrency() {
        return currencies.values().stream()
            .filter(Currency::isPrimary)
            .findFirst()
            .orElse(null);
    }

    public Collection<Currency> getAllCurrencies() {
        return Collections.unmodifiableCollection(currencies.values());
    }
}
