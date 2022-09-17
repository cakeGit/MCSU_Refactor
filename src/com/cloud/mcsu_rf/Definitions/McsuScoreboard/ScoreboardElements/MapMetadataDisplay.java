package com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements;

import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElementBase;
import org.bukkit.ChatColor;

public class MapMetadataDisplay extends ScoreboardElementBase {

    boolean displayOnUnavailable;

    public MapMetadataDisplay(boolean displayOnUnavailable) {
        instances.add(this);

        this.displayOnUnavailable = displayOnUnavailable;
    }

    public String[] generateContent(McsuPlayer player) {

        if (!displayOnUnavailable && Game.gameActive) {
            return new String[]{"",
                    ChatColor.RED +"Game: "+ ChatColor.RESET + Game.mapMetadata.getGame(),
                    ChatColor.RED +"Map: "+ ChatColor.RESET + Game.mapMetadata.getName(),
                    ChatColor.RED +"Creator: "+ ChatColor.RESET + Game.mapMetadata.getCreator()
            };
        }

        return new String[0];
    }

}
