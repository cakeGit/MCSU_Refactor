package com.cloud.mcsu_rf.Inventories;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Objects;

public class SpleefInventory extends InventoryBase {

    String tool;

    public SpleefInventory(String tool) {

        this.tool = tool;

    }

    public void onBoundEvent(Event event) {

    }

    public void loadInventory(Player player) {

        Inventory playerInventory = player.getInventory();

        ItemStack toolItemStack;

        switch (tool) {
            case "Shovels":

                toolItemStack = new ItemStack(
                        Material.NETHERITE_SHOVEL
                );
                toolItemStack.addEnchantment(
                        Enchantment.DIG_SPEED,
                        5
                );

                playerInventory.setItem(
                        0,
                        toolItemStack
                );

                break;
            case "Fireworks":

                toolItemStack = new ItemStack(
                        Material.CROSSBOW
                );
                toolItemStack.addEnchantment(
                        Enchantment.QUICK_CHARGE,
                        3
                );
                toolItemStack.addEnchantment(
                        Enchantment.DURABILITY,
                        3
                );

                ItemStack fireworksItemStack = new ItemStack(
                        Material.FIREWORK_ROCKET
                );

                fireworksItemStack.setAmount(64);
                FireworkMeta fireworkMeta = (FireworkMeta) fireworksItemStack.getItemMeta();
                fireworkMeta.setPower(100);
                fireworkMeta.addEffect(
                        FireworkEffect
                        .builder()
                        .withFade(Color.WHITE)
                        .withColor(Color.AQUA)
                        .trail(true)
                        .build()
                );
                fireworksItemStack.setItemMeta(fireworkMeta);

                playerInventory.setItem(
                        0,
                        toolItemStack
                );
                player.getInventory().setItemInOffHand(
                        fireworksItemStack
                );

                break;
        }

    }

    public void reloadInventory(Player player) {

        if (Objects.equals(tool, "Fireworks")) {

            Inventory playerInventory = player.getInventory();

            ItemStack fireworksItemStack = new ItemStack(
                    Material.FIREWORK_ROCKET
            );

            fireworksItemStack.setAmount(64);
            FireworkMeta fireworkMeta = (FireworkMeta) fireworksItemStack.getItemMeta();
            fireworkMeta.addEffect(
                    FireworkEffect
                            .builder()
                            .withFade(Color.AQUA)
                            .withColor(Color.WHITE)
                            .trail(true)
                            .build()
            );
            fireworksItemStack.setItemMeta(fireworkMeta);

            player.getInventory().setItemInOffHand(
                    fireworksItemStack
            );

        }

    }

}
