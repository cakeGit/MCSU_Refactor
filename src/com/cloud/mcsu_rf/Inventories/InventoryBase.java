package com.cloud.mcsu_rf.Inventories;

import com.cloud.mcsu_rf.Objects.GameFunctions.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;

public class InventoryBase {

    public void load(Player player) {}
    public void init() {}
    public void onBoundEvent(Event event) {}
    public void onInventoryEvent(String inventoryEventName) {}

    protected InventoryManager manager;
    public void setManager(InventoryManager manager) { this.manager = manager; }

}
