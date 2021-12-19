package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.ChatColor;

import java.util.Objects;

import static org.bukkit.ChatColor.RED;

public class SkybattleInventory {

    public void loadInventory(Player player) {

        Inventory playerInventory = player.getInventory();
        playerInventory.setItem(0,new ItemStack(Material.STONE_SWORD,1));
        ItemStack ironpick = new ItemStack(Material.IRON_PICKAXE,1);
        ironpick.addEnchantment(Enchantment.DIG_SPEED,2);
        playerInventory.setItem(1,ironpick);
        playerInventory.setItem(2,new ItemStack(Material.COOKED_BEEF,8));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE,1));
        ItemStack blocks = new ItemStack(Material.WHITE_CONCRETE,64);
        McsuTeam team = TeamMain.getTeamById(McsuPlayer.getByBukkitPlayer(player).getTeamID());
        switch (team.getChatColour()) {
            case "§c":
                blocks.setType(Material.RED_CONCRETE);
                blocks.setAmount(64);
            case "§a":
                blocks.setType(Material.GREEN_CONCRETE);
                blocks.setAmount(64);
            case "§e":
                blocks.setType(Material.YELLOW_CONCRETE);
                blocks.setAmount(64);
            case "§b":
                blocks.setType(Material.LIGHT_BLUE_CONCRETE);
                blocks.setAmount(64);
            case "§d":
                blocks.setType(Material.PINK_CONCRETE);
                blocks.setAmount(64);
            case "§f":
                blocks.setType(Material.WHITE_CONCRETE);
                blocks.setAmount(64);
            case "§7":
                blocks.setType(Material.GRAY_CONCRETE);
                blocks.setAmount(64);
            case "§9":
                blocks.setType(Material.BLUE_CONCRETE);
                blocks.setAmount(64);
        }
        player.getInventory().setItemInOffHand(blocks);
    }

    public void reloadInventory(Player player) {
        ItemStack blocks = new ItemStack(Material.WHITE_CONCRETE,64);
        McsuTeam team = TeamMain.getTeamById(McsuPlayer.getByBukkitPlayer(player).getTeamID());
        switch (team.getChatColour()) {
            case "§c":
                blocks.setType(Material.RED_CONCRETE);
                blocks.setAmount(64);
            case "§a":
                blocks.setType(Material.GREEN_CONCRETE);
                blocks.setAmount(64);
            case "§e":
                blocks.setType(Material.YELLOW_CONCRETE);
                blocks.setAmount(64);
            case "§b":
                blocks.setType(Material.LIGHT_BLUE_CONCRETE);
                blocks.setAmount(64);
            case "§d":
                blocks.setType(Material.PINK_CONCRETE);
                blocks.setAmount(64);
            case "§f":
                blocks.setType(Material.WHITE_CONCRETE);
                blocks.setAmount(64);
            case "§7":
                blocks.setType(Material.GRAY_CONCRETE);
                blocks.setAmount(64);
            case "§9":
                blocks.setType(Material.BLUE_CONCRETE);
                blocks.setAmount(64);
        }
        player.getInventory().setItemInOffHand(blocks);
    }

}
