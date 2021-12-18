package com.cloud.mcsu_rf.Objects.Game_Functions;

import com.cloud.mcsu_rf.Inventories.InventoryBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class PlayerInventoryManager extends GameFunctionBase {

    private final InventoryBase inventory;

    public PlayerInventoryManager(InventoryBase inventory) {

        this.boundEventNames.add("GameSpawnsActivatedEvent");
        this.inventory = inventory;

    }

    public void onBoundEvent(Event event) {

        if (event.getEventName().equals("GameSpawnsActivatedEvent")) {
            for ( Player player : Bukkit.getOnlinePlayers() ) {
                this.inventory.loadInventory(player);
            }
        }

    }

}
