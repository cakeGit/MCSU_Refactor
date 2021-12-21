package com.cloud.mcsu_rf.Objects.GameFunctions.ActionZones;

import org.bukkit.GameMode;

public class HeightKillZone extends HeightActionZone {

    public HeightKillZone(int height, boolean above) {
        super(height, above);
        super.setWhilePlayerInside(player -> {
            if (
                    player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR
            ) {
                player.setHealth(0);
            }
        });
    }

}
