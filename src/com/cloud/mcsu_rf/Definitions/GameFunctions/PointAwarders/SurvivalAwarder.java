package com.cloud.mcsu_rf.Definitions.GameFunctions.PointAwarders;

import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.GameFunctions.GameFunctionBase;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SurvivalAwarder extends GameFunctionBase {

    int pointValue;
    public SurvivalAwarder(int pointValue) {this.pointValue=pointValue;boundEventNames.add("PlayerDeathEvent");}

    public void onBoundEvent(Event event) {

        if ("PlayerDeathEvent".equals(event.getEventName())) {
            for (McsuPlayer mcsuPlayer : Game.getAlivePlayers()) {
                if(mcsuPlayer.toBukkit() != ((PlayerDeathEvent) event).getEntity()){
                    mcsuPlayer.awardPoints(pointValue, "surviving");
                }
            }
        }

    }

}
