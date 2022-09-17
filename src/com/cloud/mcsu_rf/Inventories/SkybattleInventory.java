package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.TeamSwitchStatements;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkybattleInventory extends InventoryBase {

    public void loadInventory(Player player) {

        Inventory playerInventory = player.getInventory();
        playerInventory.setItem(0,new ItemStack(Material.STONE_SWORD,1));
        ItemStack ironpick = new ItemStack(Material.IRON_PICKAXE,1);
        ironpick.addEnchantment(Enchantment.DIG_SPEED,2);
        playerInventory.setItem(1,ironpick);
        playerInventory.setItem(2,new ItemStack(Material.COOKED_BEEF,8));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE,1));
        player.getInventory().setItemInOffHand(TeamSwitchStatements.colouredConcreteItem(McsuPlayer.fromBukkit(player).getTeamID()));
    }

    public void reloadInventory(Player player) {
        player.getInventory().setItemInOffHand(TeamSwitchStatements.colouredConcreteItem(McsuPlayer.fromBukkit(player).getTeamID()));
    }

}