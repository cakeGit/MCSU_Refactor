package com.cloud.mcsu_rf.Objects.LootTable;

import org.bukkit.inventory.ItemStack;

public class LootOption {

    int weight;
    ItemStack item;
    int minQuantity;
    int maxQuantity;

    public LootOption(int weight, ItemStack item) {

        this.weight = weight;
        this.item = item;

        minQuantity = item.getAmount();
        maxQuantity = item.getAmount();

    }

    public LootOption setMinQuantity(int minQuantity) { this.minQuantity = minQuantity; return this; }
    public LootOption setMaxQuantity(int maxQuantity) { this.maxQuantity = maxQuantity; return this; }

    public int getWeight() { return weight; }
    public ItemStack getItem() { return item; }

}
