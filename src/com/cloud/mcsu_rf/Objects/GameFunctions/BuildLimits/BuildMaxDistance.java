package com.cloud.mcsu_rf.Objects.GameFunctions.BuildLimits;

import com.cloud.mcsu_rf.Objects.GameFunctions.GameFunctionBase;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildMaxDistance extends GameFunctionBase {

    private final float originX;
    private final float originZ;
    private final float maxDistance;

    public BuildMaxDistance(float originX, float originZ, float maxDistance) {

        this.originX = originX;
        this.originZ = originZ;
        this.maxDistance = maxDistance;

        boundEventNames.add("BlockPlaceEvent");

    }

    @Override
    public void onBoundEvent(Event event) {
        if (event.getEventName().equals("BlockPlaceEvent")) {
            BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
            Location blockLocation = placeEvent.getBlockPlaced().getLocation();

            if (
                    Math.abs(blockLocation.getX() - originX)
                            >= maxDistance
                            ||
                            Math.abs(blockLocation.getZ() - originZ)
                                    >= maxDistance
            ) {
                placeEvent.setCancelled(true);
                placeEvent.getPlayer().sendMessage(ChatColor.RED+"Hey! You cant build there!");
            }
        }
    }
}
