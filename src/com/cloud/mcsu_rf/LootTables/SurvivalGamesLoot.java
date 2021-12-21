package com.cloud.mcsu_rf.LootTables;

import com.cloud.mcsu_rf.Objects.LootTable.LootOption;
import com.cloud.mcsu_rf.Objects.LootTable.LootTable;
import com.cloud.mcsu_rf.Objects.McsuItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SurvivalGamesLoot {

    public static LootTable chestLootTable;

    public static void init() {

        chestLootTable = new LootTable(
                new LootOption(
                        1,
                        new McsuItemStack(Material.DIAMOND_BOOTS,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_FALL,2)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.DIAMOND_LEGGINGS,1)
                                .forceAddEnchantment(Enchantment.DURABILITY,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.IRON_BOOTS,1)
                                .forceAddEnchantment(Enchantment.DEPTH_STRIDER,3)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.IRON_LEGGINGS,1)
                                .forceAddEnchantment(Enchantment.THORNS,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.IRON_CHESTPLATE,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.IRON_HELMET,1)
                                .forceAddEnchantment(Enchantment.WATER_WORKER,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.GOLDEN_CHESTPLATE,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1)
                )
        );

    }

}
