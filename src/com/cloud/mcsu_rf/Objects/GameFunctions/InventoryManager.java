package com.cloud.mcsu_rf.Objects.GameFunctions;

import com.cloud.mcsu_rf.Inventories.BlockSumoInventory;
import com.cloud.mcsu_rf.Inventories.InventoryBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class InventoryManager extends GameFunctionBase {

    protected InventoryBase inventory;

    public InventoryManager(InventoryBase inventory) {

        inventory.setManager(this);
        this.inventory = inventory;
        boundEventNames.add("GameSpawnsActivatedEvent");

    }

    public void addBoundEvent(String boundEventName) {
        boundEventNames.add(boundEventName);
    }

    public void onBoundEvent(Event event) {

        if(event.getEventName().equals("GameSpawnsActivatedEvent")) {
            for (Player player : Bukkit.getOnlinePlayers())
                inventory.load(player);
        }

    }
}
