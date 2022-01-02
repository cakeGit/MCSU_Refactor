package com.cloud.mcsu_rf.Objects.GameFunctions;

import com.cloud.mcsu_rf.Objects.Enums.PointGoal;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.event.Event;

import java.awt.*;

public class PointAwarder extends GameFunction {

    protected int pointValue;

    public PointAwarder(PointGoal Goal, int pointValue) {

        this.pointValue = pointValue;

        switch (Goal) {
            case Survival -> {
                boundEventNames.add("PlayerDeathEvent");
                break;
            }
        }

    }

    public void onBoundEvent(Event event) {

        if ("PlayerDeathEvent".equals(event.getEventName())) {
            for (McsuPlayer mcsuPlayer : McsuPlayer.McsuPlayers) {
                mcsuPlayer.awardPoints(pointValue);
            }
        }

    }

}
