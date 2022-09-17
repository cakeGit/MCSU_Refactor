package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.Definitions.GameFunctions.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class InventoryBase {

    public void load(Player player) {}
    public void init() {}
    public void gameInit() {}
    public void onBoundEvent(Event event) {}
    public void onInventoryEvent(String inventoryEventName) {}

    protected InventoryManager manager;
    public void setManager(InventoryManager manager) { this.manager = manager; }

}
