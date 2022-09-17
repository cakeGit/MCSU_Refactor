package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.LootTables.SurvivalGamesLoot;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SurvivalGamesInventory implements InventoryHolder {

    private Inventory inv;

    public SurvivalGamesInventory() {
        inv = Bukkit.createInventory(this, 27);
        init();
    }

    private void init() {
        inv.clear();
        Random rand = new Random();
        ItemStack item1 = SurvivalGamesLoot.chestLootTable.generate();
        inv.setItem(rand.nextInt(27),item1);
        ItemStack item2 = SurvivalGamesLoot.chestLootTable.generate();
        inv.setItem(rand.nextInt(27),item2);
        ItemStack item3 = SurvivalGamesLoot.chestLootTable.generate();
        inv.setItem(rand.nextInt(27),item3);
        ItemStack item4 = SurvivalGamesLoot.chestLootTable.generate();
        inv.setItem(rand.nextInt(27),item4);
        ItemStack item5 = SurvivalGamesLoot.chestLootTable.generate();
        inv.setItem(rand.nextInt(27),item5);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
