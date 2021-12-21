package com.cloud.mcsu_rf.Objects.GameFunctions.BuildLimits;

import com.cloud.mcsu_rf.Objects.GameFunctions.GameFunction;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildMaxHeight extends GameFunction {

    private final int maxHeight;

    public BuildMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        boundEventNames.add("BlockPlaceEvent");

    }

    @Override
    public void onBoundEvent(Event event) {
        if (event.getEventName().equals("BlockPlaceEvent")) {
            BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;

            if (
                    placeEvent.getBlockPlaced().getLocation().getY()
                            >= maxHeight
            ) {
                placeEvent.setCancelled(true);
                placeEvent.getPlayer().sendMessage(ChatColor.RED+"Hey! You cant build there!");
            }
        }
    }
}
