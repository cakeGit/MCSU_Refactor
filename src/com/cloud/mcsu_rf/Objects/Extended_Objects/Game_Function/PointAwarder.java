package com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function;

import com.cloud.mcsu_rf.Objects.MCSU_Player;
import org.bukkit.event.Event;

public class PointAwarder extends GameFunctionBase {

    public PointAwarder(String Goal, int pointValue) {

        switch (Goal) {
            case "Survival": this.boundEventNames.add("PlayerDeathEvent"); break;
        }

    }

    public void runEventHandler(Event event) {

        switch (event.getEventName()) {
            case "PlayerDeathEvent":
                for (MCSU_Player mcsuPlayer : MCSU_Player.MCSU_Players) {
                    mcsuPlayer.awardPoints(2);
                }
                break;
        }

    }

}
