package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.McsuItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SnowballFightInventory extends InventoryBase {

    String weapon;

    public static McsuItemStack arrow;

    public void gameInit() {
        weapon = Game.currentGame.getGamemodeOptionBlockChoice("Weapon").getGamemodeOption().getName();

        arrow = new McsuItemStack(Material.ARROW)
                .setName(ChatColor.ITALIC +""+ ChatColor.AQUA + "Snowball on a stick")
                .setBasicLore(ChatColor.GRAY + "A snowball on a stick. - Fired from a launcher.");
    }

    public void load(Player player) {

        Inventory playerInventory = player.getInventory();


        switch (weapon) {
            case "Snowballs":
                playerInventory.setItem(0, new ItemStack(Material.SNOWBALL, 5));
                break;
            case "Launchers":
                //make an unbreakable bow
                McsuItemStack snowballLauncher = new McsuItemStack(Material.BOW);
                snowballLauncher.setUnbreakable(true);
                snowballLauncher.setName(ChatColor.ITALIC +""+ ChatColor.AQUA + "Snowball Launcher");
                snowballLauncher.setBasicLore(ChatColor.GRAY + "(Not a bow) Launcher that can shoots snowballs on sticks.");
                playerInventory.setItem(0, snowballLauncher);

                //give arrows
                playerInventory.setItem(1, arrow.clone().setAmountRI(3));
                break;
            case "Guns":
                //make an unbreakable crossbow
                McsuItemStack snowballGun = new McsuItemStack(Material.CROSSBOW);
                snowballGun.setUnbreakable(true);
                snowballGun.setName(ChatColor.ITALIC +""+ ChatColor.AQUA + "Snowball Gun");
                snowballGun.setBasicLore(ChatColor.GRAY + "(Not a bow) Gun that can shoots snowballs on sticks.");
                playerInventory.setItem(0, snowballGun);

                //give arrows
                playerInventory.setItem(1, arrow);
                break;
        }

    }

}
