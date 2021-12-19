package com.cloud.mcsu_rf.Objects.Game_Functions;

import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.event.Event;

public class PointAwarder extends GameFunctionBase {

    public PointAwarder(String Goal, int pointValue) {

        switch (Goal) {
            case "Survival": boundEventNames.add("PlayerDeathEvent"); break;
        }

    }

    public void onBoundEvent(Event event) {

        if ("PlayerDeathEvent".equals(event.getEventName())) {
            for (McsuPlayer mcsuPlayer : McsuPlayer.McsuPlayers) {
                mcsuPlayer.awardPoints(2);
            }
        }

    }

}
