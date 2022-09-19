package com.cloud.mcsu_rf.LootTables;

import com.cak.what.ItemAPI.WhItemStack;
import com.cloud.mcsu_rf.Definitions.LootTable.LootOption;
import com.cloud.mcsu_rf.Definitions.LootTable.LootTable;
import com.cloud.mcsu_rf.Definitions.McsuItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Random;

public class SurvivalGamesLoot {

    public static LootTable chestLootTable;

    public static void init() {

        ItemStack regen = new ItemStack(Material.SPLASH_POTION,1);
        PotionMeta regen_pot = (PotionMeta) regen.getItemMeta();
        regen_pot.setBasePotionData(new PotionData(PotionType.REGEN));
        regen.setItemMeta(regen_pot);

        int[] arrowamount = {16,32};

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
                        new McsuItemStack(Material.DIAMOND_CHESTPLATE,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_PROJECTILE,1)
                ),
                new LootOption(
                        1,
                        new McsuItemStack(Material.DIAMOND_HELMET,1)
                                .forceAddEnchantment(Enchantment.OXYGEN,2)
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.IRON_BOOTS,1)
                                .forceAddEnchantment(Enchantment.DEPTH_STRIDER,3)
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.IRON_LEGGINGS,1)
                                .forceAddEnchantment(Enchantment.THORNS,1)
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.IRON_CHESTPLATE,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1)
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.IRON_HELMET,1)
                                .forceAddEnchantment(Enchantment.WATER_WORKER,1)
                ),
                new LootOption(
                        3,
                        new McsuItemStack(Material.GOLDEN_CHESTPLATE,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1)
                ),
                new LootOption(
                        3,
                        new McsuItemStack(Material.GOLDEN_HELMET,1)
                                .forceAddEnchantment(Enchantment.OXYGEN,3)
                ),
                new LootOption(
                        4,
                        new McsuItemStack(Material.LEATHER_BOOTS,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_FALL,4)
                                .forceAddEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1)
                ),
                new LootOption(
                        3,
                        new McsuItemStack(Material.LEATHER_LEGGINGS,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,3)
                ),
                new LootOption(
                        3,
                        new McsuItemStack(Material.LEATHER_CHESTPLATE,1)
                                .forceAddEnchantment(Enchantment.DURABILITY,3)
                                .forceAddEnchantment(Enchantment.PROTECTION_PROJECTILE,4)
                ),
                new LootOption(4,
                        new McsuItemStack(Material.LEATHER_HELMET,1)
                                .forceAddEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2)
                ),
                new LootOption(3,
                        new McsuItemStack(Material.BOW,1)
                                .forceAddEnchantment(Enchantment.ARROW_KNOCKBACK,1)
                ),
                new LootOption(
                        3,
                        regen
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.GOLDEN_APPLE,new Random().nextInt(2)+1)
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.DIAMOND_SWORD,1)
                                .forceAddEnchantment(Enchantment.SWEEPING_EDGE,2)
                ),
                new LootOption(
                        3,
                        new McsuItemStack(Material.IRON_SWORD,1)
                                .forceAddEnchantment(Enchantment.DAMAGE_ALL,1)
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.IRON_AXE,1)
                ),
                new LootOption(
                        4,
                        new McsuItemStack(Material.WOODEN_SWORD,1)
                                .forceAddEnchantment(Enchantment.KNOCKBACK,2)
                ),
                new LootOption(
                        5,
                        new McsuItemStack(Material.COOKED_BEEF,8)
                ),
                new LootOption(
                        5,
                        new McsuItemStack(Material.BAKED_POTATO,16)
                ),
                new LootOption(
                        4,
                        new McsuItemStack(Material.ARROW,arrowamount[new Random().nextInt(arrowamount.length)])
                ),
                new LootOption(
                        2,
                        new McsuItemStack(Material.ENDER_PEARL,1)
                ),
                new LootOption(
                        3,
                        new McsuItemStack(Material.CROSSBOW, 1)
                                .forceAddEnchantment(Enchantment.QUICK_CHARGE,2)
                )
        );

    }

}
