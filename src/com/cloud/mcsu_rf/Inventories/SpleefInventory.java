package com.cloud.mcsu_rf.Inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SpleefInventory extends InventoryBase {

    public void loadInventory(Player player) {

        Inventory playerInventory = player.getInventory();

        ItemStack shovel = new ItemStack(
                Material.NETHERITE_SHOVEL
            );
        shovel.addEnchantment(
                        Enchantment.DIG_SPEED,
                        5
                );

        playerInventory.setItem(
                0,
                shovel
                );

    }

}
