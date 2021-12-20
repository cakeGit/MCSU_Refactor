package com.cloud.mcsu_rf;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TeamSwitchStatements {

    public static ItemStack colouredConcreteItem(String teamId) {

        ItemStack blocks = new ItemStack(Material.WHITE_CONCRETE,64);

        switch (teamId) {
            case "r":
                blocks.setType(Material.RED_CONCRETE);
                break;
            case "g":
                blocks.setType(Material.GREEN_CONCRETE);
                break;
            case "y":
                blocks.setType(Material.YELLOW_CONCRETE);
                break;
            case "s":
                blocks.setType(Material.LIGHT_BLUE_CONCRETE);
                break;
            case "p":
                blocks.setType(Material.PINK_CONCRETE);
                break;
            case "w":
                blocks.setType(Material.WHITE_CONCRETE);
                break;
            case "gr":
                blocks.setType(Material.GRAY_CONCRETE);
                break;
            case "b":
                blocks.setType(Material.BLUE_CONCRETE);
                break;
        }

        return blocks;

    }

    public static ItemStack colouredWoolItem(String teamId) {
        ItemStack blocks = new ItemStack(Material.WHITE_WOOL,64);

        switch (teamId) {
            case "r":
                blocks.setType(Material.RED_WOOL);
                break;
            case "g":
                blocks.setType(Material.GREEN_WOOL);
                break;
            case "y":
                blocks.setType(Material.YELLOW_WOOL);
                break;
            case "s":
                blocks.setType(Material.LIGHT_BLUE_WOOL);
                break;
            case "p":
                blocks.setType(Material.PINK_WOOL);
                break;
            case "w":
                blocks.setType(Material.WHITE_WOOL);
                break;
            case "gr":
                blocks.setType(Material.GRAY_WOOL);
                break;
            case "b":
                blocks.setType(Material.BLUE_WOOL);
                break;
        }

        return blocks;
    }
}
