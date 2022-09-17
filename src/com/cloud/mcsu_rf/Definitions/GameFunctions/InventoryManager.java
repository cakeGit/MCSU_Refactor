package com.cloud.mcsu_rf.Definitions.GameFunctions;

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
        boundEventNames.add("GameInitEvent");

    }

    public void bindEvent(String eventName) {
        boundEventNames.add(eventName);
    }

    public void onBoundEvent(Event event) {

        inventory.onBoundEvent(event);

        if(event.getEventName().equals("GameSpawnsActivatedEvent")) {
            for (Player player : Bukkit.getOnlinePlayers())
                inventory.load(player);
        } else if(event.getEventName().equals("GameInitEvent")) {
            inventory.gameInit();
        }

    }

    public void emitInventoryEvent(String inventoryEventName) { inventory.onInventoryEvent(inventoryEventName); }

}
