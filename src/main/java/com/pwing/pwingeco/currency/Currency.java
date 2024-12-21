package com.pwing.pwingeco.currency;

import org.bukkit.inventory.ItemStack;

public class Currency {
    private final String name;
    private final String symbol;
    private final boolean isPrimary;
    private final ItemStack itemRepresentation;
    
    public Currency(String name, String symbol, boolean isPrimary, ItemStack itemRepresentation) {
        this.name = name;
        this.symbol = symbol;
        this.isPrimary = isPrimary;
        this.itemRepresentation = itemRepresentation;
    }
    
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public boolean isPrimary() { return isPrimary; }
    public ItemStack getItemRepresentation() { return itemRepresentation; }
}
