package com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZones;

import org.bukkit.GameMode;
import org.bukkit.event.entity.EntityDamageEvent;

public class HeightKillZone extends HeightActionZone {

    public HeightKillZone(int height, boolean above) {
        super(height, above);
        super.setWhilePlayerInside(player -> {
            if (
                    player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR
            ) {
                player.setHealth(0);
                player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, 20));
            }
        });
    }

}
