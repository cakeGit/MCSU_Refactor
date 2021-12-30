package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.LootTables.BlockSumoLoot;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuItemStack;
import com.cloud.mcsu_rf.TeamSwitchStatements;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

public class BlockSumoInventory extends InventoryBase {

    public void init() {
        manager.bindEvent("BlockPlaceEvent");
    }

    public void load(Player player) {

        Inventory playerInventory = player.getInventory();

        playerInventory.setItem(
                0,
                new McsuItemStack(Material.SHEARS,1)
                        .setUnbreakable(true)
        );

        ItemStack blocks = TeamSwitchStatements.colouredWoolItem(McsuPlayer.fromBukkit(player).getTeamID());
        player.getInventory().setItemInOffHand(blocks);

    }

    public void onBoundEvent(Event event) {

        if(event.getEventName().equals("BlockPlaceEvent")) {
            BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
            PlayerInventory playerInventory = placeEvent.getPlayer().getInventory();
            EquipmentSlot slot = placeEvent.getHand();

            if (
                    playerInventory.getItem(slot).getType()
                            ==
                            TeamSwitchStatements.colouredWoolItem(
                                    McsuPlayer.fromBukkit(placeEvent.getPlayer()).getTeamID()
                            ).getType()
            ) {
                playerInventory.getItem(slot).setAmount(64);
            }
        }
    }

    public void onInventoryEvent(String inventoryEventName) {
        if (Objects.equals(inventoryEventName, "Powerup")) {
            for (McsuPlayer player : Game.getAlivePlayers()) {
                givePowerUp(player.toBukkit());
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
