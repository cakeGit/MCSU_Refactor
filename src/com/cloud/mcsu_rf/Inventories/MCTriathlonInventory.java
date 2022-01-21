package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.McsuItemStack;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.TeamSwitchStatements;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MCTriathlonInventory extends InventoryBase {

    public void loadArea1(McsuPlayer player) {
        McsuItemStack trident = new McsuItemStack(Material.TRIDENT,1);
        trident.forceAddEnchantment(Enchantment.RIPTIDE,3);
        trident.setUnbreakable(true);
        trident.setName(ChatColor.AQUA+"Zeus' Fork");

        player.toBukkit().getInventory().setItemInOffHand(trident);
    }

    public void loadArea2(McsuPlayer player) {
        player.toBukkit().getInventory().clear();
        McsuItemStack elytra = new McsuItemStack(Material.ELYTRA,1);
        elytra.setName(ChatColor.GOLD+player.getName()+"'s Wings");
        elytra.setUnbreakable(true);

        player.toBukkit().getInventory().setChestplate(elytra);
    }

    public void loadArea3(McsuPlayer player) {
        player.toBukkit().getInventory().clear();
        McsuItemStack boat = new McsuItemStack(Material.SPRUCE_BOAT,1);
        boat.setName(ChatColor.DARK_GRAY+"Winter Sled");
        player.toBukkit().getInventory().setItem(0,boat);
    }

}
