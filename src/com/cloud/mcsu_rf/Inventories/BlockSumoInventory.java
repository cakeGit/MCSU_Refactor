package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.LootTables.BlockSumoLoot;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuItemStack;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import com.cloud.mcsu_rf.TeamSwitchStatements;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockSumoInventory extends InventoryBase {

    public void load(Player player) {

        Inventory playerInventory = player.getInventory();

        playerInventory.setItem(
                0,
                new McsuItemStack(Material.SHEARS,1)
                        .setUnbreakable(true)
        );

        ItemStack blocks = TeamSwitchStatements.colouredWoolItem(McsuPlayer.getByBukkitPlayer(player).getTeamID());
        player.getInventory().setItemInOffHand(blocks);

        manager.addBoundEvent("BlockPlaceEvent");

    }

    public void onBoundEvent(Event event) {

        if(event.getEventName().equals("BlockPlaceEvent")) {
            BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;

            if (
                    placeEvent.getBlockPlaced().getState().getData().toItemStack(64)
                            ==
                            TeamSwitchStatements.colouredWoolItem(
                                    McsuPlayer.getByBukkitPlayer(placeEvent.getPlayer()).getTeamID()
                            )
            ) {
                Bukkit.broadcastMessage("Thing match team");
            }
        }



    }

    public void givePowerUp(Player player) {

        Inventory playerInventory = player.getInventory();
        playerInventory.addItem(
                BlockSumoLoot.powerupLootTable.generate()
                );

    }

}
