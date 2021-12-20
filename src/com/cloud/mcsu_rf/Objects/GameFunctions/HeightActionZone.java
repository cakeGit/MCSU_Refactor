package com.cloud.mcsu_rf.Objects.GameFunctions;

import org.bukkit.Location;

public class HeightActionZone extends ActionZone {

    int activationHeight;

    public HeightActionZone(int activationHeight, boolean below) {

        super(null, null);

        this.activationHeight = activationHeight;

    }

    public boolean testLocationInsideZone(Location loc) {
        return loc.getY() < activationHeight;
    }
}
