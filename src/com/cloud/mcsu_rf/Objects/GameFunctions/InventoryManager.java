package com.cloud.mcsu_rf.Objects.GameFunctions;

import com.cloud.mcsu_rf.Inventories.InventoryBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class InventoryManager extends GameFunctionBase {

    protected InventoryBase inventory;

    public InventoryManager(InventoryBase inventory) {

        inventory.setManager(this);
        this.inventory = inventory;
        inventory.init();
        boundEventNames.add("GameSpawnsActivatedEvent");

    }

    public void bindEvent(String eventName) {
        boundEventNames.add(eventName);
    }

    public void onBoundEvent(Event event) {

        inventory.onBoundEvent(event);

        if(event.getEventName().equals("GameSpawnsActivatedEvent")) {
            for (Player player : Bukkit.getOnlinePlayers())
                inventory.load(player);
        }

    }

    public void emitInventoryEvent(String inventoryEventName) { inventory.onInventoryEvent(inventoryEventName); }

}
