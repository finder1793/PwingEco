package com.pwing.pwingeco.currency;

import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class Currency {
    private final String name;
    private final String symbol;
    private final boolean primary;
    private final ItemStack itemRepresentation;
    private final boolean tradeable;
    private final List<String> description;
    private final boolean global;
    private final List<String> enabledWorlds;
    private final boolean itemBased;
    private final int maxStackSize;
    private final ItemStack nextTierItem;
    private final int combineAmount;

    public Currency(String name, String symbol, boolean primary, ItemStack itemRepresentation, boolean tradeable, List<String> description, boolean itemBased, int maxStackSize, ItemStack nextTierItem, int combineAmount) {
        this.name = name;
        this.symbol = symbol;
        this.primary = primary;
        this.itemRepresentation = itemRepresentation;
        this.tradeable = tradeable;
        this.description = description;
        this.global = true;
        this.enabledWorlds = new ArrayList<>();
        this.itemBased = itemBased;
        this.maxStackSize = maxStackSize;
        this.nextTierItem = nextTierItem;
        this.combineAmount = combineAmount;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isPrimary() {
        return primary;
    }

    public ItemStack getItemRepresentation() {
        return itemRepresentation != null ? itemRepresentation.clone() : null;
    }

    public boolean isTradeable() {
        return tradeable;
    }

    public List<String> getDescription() {
        return description;
    }

    public boolean isGlobal() {
        return global;
    }

    public List<String> getEnabledWorlds() {
        return new ArrayList<>(enabledWorlds);
    }

    public boolean isEnabledInWorld(String worldName) {
        return global || enabledWorlds.contains(worldName);
    }

    public boolean isItemBased() {
        return itemBased;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public ItemStack getNextTierItem() {
        return nextTierItem != null ? nextTierItem.clone() : null;
    }

    public int getCombineAmount() {
        return combineAmount;
    }
}
