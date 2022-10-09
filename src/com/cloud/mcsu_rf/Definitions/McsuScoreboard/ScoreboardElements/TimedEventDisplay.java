package com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements;

import com.cak.what.Util.ChCol;
import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElementBase;

import java.util.Map;
import java.util.Objects;

public class  TimedEventDisplay extends ScoreboardElementBase {


    public TimedEventDisplay() {
        instances.add(this);
    }

    public String formatTime(Double time) {
        return ((int) Math.floor(time/60)) + ":" + ((int) (time % 60));
    }

    public String[] generateContent(McsuPlayer player) {

        if (Game.gameActive) {

            String currentTimedEvent = "";
            Integer currentTimedTime = (int) Double.POSITIVE_INFINITY;

            for (Map.Entry<String, Integer> timedEvent : Game.QueuedTimedEvents.entrySet()) {
                if (timedEvent.getValue() < currentTimedTime) {
                    currentTimedEvent = timedEvent.getKey();
                    currentTimedTime = timedEvent.getValue();
                }
            }

            if (!Objects.equals(currentTimedEvent, "")) {
                return new String[]{ "",
                        ChCol.BLUE + ChCol.BOLD + currentTimedEvent + ChCol.RESET + ": " + formatTime(Double.valueOf(currentTimedTime))
                };
            }

            return new String[]{ };
        }

        return new String[0];
    }

}
