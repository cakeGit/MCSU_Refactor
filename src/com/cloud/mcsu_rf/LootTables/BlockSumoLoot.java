package com.cloud.mcsu_rf.LootTables;

import com.cloud.mcsu_rf.Objects.LootTable.LootOption;
import com.cloud.mcsu_rf.Objects.LootTable.LootTable;
import com.cloud.mcsu_rf.Objects.McsuItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BlockSumoLoot {

    public static LootTable powerupLootTable;

    public static void init() {

        powerupLootTable = new LootTable(
                new LootOption(
                        3,
                        new ItemStack(Material.FISHING_ROD)
                ),
                new LootOption(
                        4,
                        new ItemStack(Material.TNT)
                ),
                new LootOption(
                        4,
                        new ItemStack(Material.COBWEB, 4)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.STICK)
                                .forceAddEnchantment(Enchantment.KNOCKBACK, 2)
                                .setName("Stick - Knockback II")
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.STICK)
                                .forceAddEnchantment(Enchantment.KNOCKBACK, 1)
                                .setName("Stick - Knockback I")
                ),
                new LootOption(
                        3,
                        new ItemStack(Material.ENDER_PEARL)
                )
        );

    }

}
