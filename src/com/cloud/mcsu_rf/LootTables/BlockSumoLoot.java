package com.cloud.mcsu_rf.LootTables;

import com.cloud.mcsu_rf.Definitions.LootTable.LootOption;
import com.cloud.mcsu_rf.Definitions.LootTable.LootTable;
import com.cloud.mcsu_rf.Definitions.McsuItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BlockSumoLoot {

    public static LootTable powerupLootTable;

    public static void init() {

        powerupLootTable = new LootTable(
                new LootOption(
                        1,
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
                        3,
                        new McsuItemStack(Material.STICK)
                                .forceAddEnchantment(Enchantment.KNOCKBACK, 1)
                                .setName("Stick - Knockback I")
                ),
                new LootOption(
                        3,
                        new ItemStack(Material.ENDER_PEARL)
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.FIRE_CHARGE)
                                .setName(ChatColor.GOLD+"Fireball")
                )
        );

    }

}
