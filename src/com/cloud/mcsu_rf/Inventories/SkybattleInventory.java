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
        McsuTeam team = TeamMain.getTeamById(McsuPlayer.getByBukkitPlayer(player).getTeamID());
        switch (team.getChatColour()) {
            case "§c":
                ItemStack redBlocks = new ItemStack(Material.RED_CONCRETE,64);
                player.getInventory().setItemInOffHand(redBlocks);
                break;
            case "§a":
                ItemStack greenBlocks = new ItemStack(Material.GREEN_CONCRETE,64);
                player.getInventory().setItemInOffHand(greenBlocks);
                break;
            case "§e":
                ItemStack yellowBlocks = new ItemStack(Material.YELLOW_CONCRETE,64);
                player.getInventory().setItemInOffHand(yellowBlocks);
                break;
            case "§b":
                ItemStack aquaBlocks = new ItemStack(Material.LIGHT_BLUE_CONCRETE,64);
                player.getInventory().setItemInOffHand(aquaBlocks);
                break;
            case "§d":
                ItemStack pinkBlocks = new ItemStack(Material.PINK_CONCRETE,64);
                player.getInventory().setItemInOffHand(pinkBlocks);
                break;
            case "§f":
                ItemStack whiteBlocks = new ItemStack(Material.WHITE_CONCRETE,64);
                player.getInventory().setItemInOffHand(whiteBlocks);
                break;
            case "§7":
                ItemStack grayBlocks = new ItemStack(Material.GRAY_CONCRETE,64);
                player.getInventory().setItemInOffHand(grayBlocks);
                break;
            case "§9":
                ItemStack blueBlocks = new ItemStack(Material.BLUE_CONCRETE,64);
                player.getInventory().setItemInOffHand(blueBlocks);
                break;
        }
    }

    public void reloadInventory(Player player) {
        if(player.getInventory().getItemInOffHand().getType().equals(Material.BLUE_CONCRETE) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.RED_CONCRETE) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.GREEN_CONCRETE) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.YELLOW_CONCRETE) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.LIGHT_BLUE_CONCRETE) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.PINK_CONCRETE) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.GRAY_CONCRETE) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.WHITE_CONCRETE)) {
            switch(player.getInventory().getItemInOffHand().getType()) {
                case BLUE_CONCRETE:
                    ItemStack blueBlocks = new ItemStack(Material.BLUE_CONCRETE,64);
                    player.getInventory().setItemInOffHand(blueBlocks);
                    break;
                case RED_CONCRETE:
                    ItemStack redBlocks = new ItemStack(Material.RED_CONCRETE,64);
                    player.getInventory().setItemInOffHand(redBlocks);
                    break;
                case GREEN_CONCRETE:
                    ItemStack greenBlocks = new ItemStack(Material.GREEN_CONCRETE,64);
                    player.getInventory().setItemInOffHand(greenBlocks);
                    break;
                case YELLOW_CONCRETE:
                    ItemStack yellowBlocks = new ItemStack(Material.YELLOW_CONCRETE,64);
                    player.getInventory().setItemInOffHand(yellowBlocks);
                    break;
                case LIGHT_BLUE_CONCRETE:
                    ItemStack aquaBlocks = new ItemStack(Material.LIGHT_BLUE_CONCRETE,64);
                    player.getInventory().setItemInOffHand(aquaBlocks);
                    break;
                case PINK_CONCRETE:
                    ItemStack pinkBlocks = new ItemStack(Material.PINK_CONCRETE,64);
                    player.getInventory().setItemInOffHand(pinkBlocks);
                    break;
                case GRAY_CONCRETE:
                    ItemStack grayBlocks = new ItemStack(Material.GRAY_CONCRETE,64);
                    player.getInventory().setItemInOffHand(grayBlocks);
                    break;
                case WHITE_CONCRETE:
                    ItemStack whiteBlocks = new ItemStack(Material.WHITE_CONCRETE,64);
                    player.getInventory().setItemInOffHand(whiteBlocks);
                    break;
            }
        } else if(player.getInventory().getItemInMainHand().getType().equals(Material.BLUE_CONCRETE) ||
                player.getInventory().getItemInMainHand().getType().equals(Material.RED_CONCRETE) ||
                player.getInventory().getItemInMainHand().getType().equals(Material.GREEN_CONCRETE) ||
                player.getInventory().getItemInMainHand().getType().equals(Material.YELLOW_CONCRETE) ||
                player.getInventory().getItemInMainHand().getType().equals(Material.LIGHT_BLUE_CONCRETE) ||
                player.getInventory().getItemInMainHand().getType().equals(Material.PINK_CONCRETE) ||
                player.getInventory().getItemInMainHand().getType().equals(Material.GRAY_CONCRETE) ||
                player.getInventory().getItemInMainHand().getType().equals(Material.WHITE_CONCRETE)) {
            switch(player.getInventory().getItemInMainHand().getType()) {
                case BLUE_CONCRETE:
                    ItemStack blueBlocks = new ItemStack(Material.BLUE_CONCRETE,64);
                    player.getInventory().setItemInMainHand(blueBlocks);
                    break;
                case RED_CONCRETE:
                    ItemStack redBlocks = new ItemStack(Material.RED_CONCRETE,64);
                    player.getInventory().setItemInMainHand(redBlocks);
                    break;
                case GREEN_CONCRETE:
                    ItemStack greenBlocks = new ItemStack(Material.GREEN_CONCRETE,64);
                    player.getInventory().setItemInMainHand(greenBlocks);
                    break;
                case YELLOW_CONCRETE:
                    ItemStack yellowBlocks = new ItemStack(Material.YELLOW_CONCRETE,64);
                    player.getInventory().setItemInMainHand(yellowBlocks);
                    break;
                case LIGHT_BLUE_CONCRETE:
                    ItemStack aquaBlocks = new ItemStack(Material.LIGHT_BLUE_CONCRETE,64);
                    player.getInventory().setItemInMainHand(aquaBlocks);
                    break;
                case PINK_CONCRETE:
                    ItemStack pinkBlocks = new ItemStack(Material.PINK_CONCRETE,64);
                    player.getInventory().setItemInMainHand(pinkBlocks);
                    break;
                case GRAY_CONCRETE:
                    ItemStack grayBlocks = new ItemStack(Material.GRAY_CONCRETE,64);
                    player.getInventory().setItemInMainHand(grayBlocks);
                    break;
                case WHITE_CONCRETE:
                    ItemStack whiteBlocks = new ItemStack(Material.WHITE_CONCRETE,64);
                    player.getInventory().setItemInMainHand(whiteBlocks);
                    break;
            }
        }
    }

}
