package com.cloud.mcsu_rf.Definitions.LootTable;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class LootTable {

    private final LootOption[] lootOptions;

    public static boolean numberInside(double a, double b, double point) { //copied from actionzone lol //copied from gamemode option lol
        return Math.abs(a-point) + Math.abs(b-point) == Math.abs(a-b);
    }

    static LootOption chooseOption( LootOption[] options ) {
        int totalWeight = 0;

        for ( LootOption option : options ) {
            totalWeight += option.getWeight();
        }

        int pickedInt = new Random().nextInt(totalWeight)+1;

        //Bukkit.broadcastMessage("Picked int "+pickedInt);

        int currentWeight = 0;

        for ( LootOption option : options ) {

            //Bukkit.broadcastMessage("Current weight "+currentWeight);
            if ( numberInside( currentWeight, currentWeight+option.getWeight(), pickedInt ) && option.getWeight() != 0) {
                //Bukkit.broadcastMessage("args: " + currentWeight +", "+ (currentWeight+option.getWeight()) +", "+ pickedInt);
                return option;
            }

            currentWeight += option.getWeight();
        }

        return null;

    }

    public LootTable(LootOption... lootOptions) {

        this.lootOptions = lootOptions;

    }

    public ItemStack generate() {
        return chooseOption(lootOptions).getItem();
    }
    public ItemStack[] generate(int length) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            itemStacks.add(chooseOption(lootOptions).getItem());
        }

        return itemStacks.toArray(new ItemStack[0]);
    }

}
