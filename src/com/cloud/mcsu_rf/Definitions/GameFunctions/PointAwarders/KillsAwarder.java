package com.cloud.mcsu_rf.Definitions.GameFunctions.PointAwarders;

import com.cloud.mcsu_rf.Definitions.GameFunctions.GameFunctionBase;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillsAwarder extends GameFunctionBase {

    int pointValue;
    public KillsAwarder(int pointValue) {this.pointValue=pointValue;boundEventNames.add("PlayerDeathEvent");}

    public void onBoundEvent(Event event) {

        if ("PlayerDeathEvent".equals(event.getEventName())) {
            PlayerDeathEvent playerDeath =  (PlayerDeathEvent) event;
            Player killer = playerDeath.getEntity().getKiller();
            if (killer != null) {
                McsuPlayer.fromBukkit(killer).awardPoints(pointValue, "killing "+playerDeath.getEntity().getName());
            }
        }

    }

}
