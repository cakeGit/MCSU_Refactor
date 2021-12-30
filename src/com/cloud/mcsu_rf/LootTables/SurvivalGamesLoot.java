package com.cloud.mcsu_rf.LootTables;

import com.cloud.mcsu_rf.Objects.LootTable.LootOption;
import com.cloud.mcsu_rf.Objects.LootTable.LootTable;
import com.cloud.mcsu_rf.Objects.McsuItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.Random;

public class SurvivalGamesLoot {

    public static LootTable chestLootTable;

    public static void init() {

        Potion fireRes = new Potion(PotionType.FIRE_RESISTANCE);
        fireRes.setSplash(true);
        fireRes.setHasExtendedDuration(true);
        fireRes.setLevel(1);
        Potion regen = new Potion(PotionType.REGEN);
        regen.setSplash(true);
        regen.setHasExtendedDuration(false);
        regen.setLevel(1);
        int[] arrowamount = {16,32};
        int[] cobwebamount = {4,8};
        int[] xpamount = {8,32,64};

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
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.GOLDEN_HELMET,1)
                                .forceAddEnchantment(Enchantment.OXYGEN,3)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.BOW,1)
                                .forceAddEnchantment(Enchantment.ARROW_KNOCKBACK,1)
                ),
                new LootOption(
                        1,
                        regen.toItemStack(1)
                ),
                new LootOption(
                        1,
                        fireRes.toItemStack(1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.GOLDEN_APPLE,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.DIAMOND_SWORD,1)
                                .forceAddEnchantment(Enchantment.SWEEPING_EDGE,2)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.IRON_SWORD,1)
                                .forceAddEnchantment(Enchantment.DAMAGE_ALL,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.IRON_AXE,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.WOODEN_SWORD,1)
                                .forceAddEnchantment(Enchantment.KNOCKBACK,5)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.COOKED_BEEF,8)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.BAKED_POTATO,16)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.ARROW,arrowamount[new Random().nextInt(arrowamount.length)])
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.COBWEB,cobwebamount[new Random().nextInt(cobwebamount.length)])
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.EXPERIENCE_BOTTLE,xpamount[new Random().nextInt(xpamount.length)])
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.ENDER_PEARL,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.LAVA_BUCKET,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.WATER_BUCKET,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.ANVIL,1)
                )
        );

    }

}
