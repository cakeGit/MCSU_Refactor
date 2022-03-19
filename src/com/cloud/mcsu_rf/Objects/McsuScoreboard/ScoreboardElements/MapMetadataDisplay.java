package com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElements;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElementBase;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class MapMetadataDisplay extends ScoreboardElementBase {

    boolean displayOnUnavailable;

    public MapMetadataDisplay(boolean displayOnUnavailable) {
        instances.add(this);

        this.displayOnUnavailable = displayOnUnavailable;
    }

    public String[] generateContent(McsuPlayer player) {

        if (!displayOnUnavailable && Game.gameActive) {
            return new String[]{"",
                    ChatColor.RED +"Game: "+ ChatColor.RESET +Game.mapMetadata.getGame(),
                    ChatColor.RED +"Map: "+ ChatColor.RESET +Game.mapMetadata.getName(),
                    ChatColor.RED +"Creator: "+ ChatColor.RESET +Game.mapMetadata.getCreator()
            };
        }

        return new String[0];
    }

}
