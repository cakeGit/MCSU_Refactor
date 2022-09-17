package com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements;

import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElementBase;

public class FixedContent extends ScoreboardElementBase {

    String[] content;

    public FixedContent(String... content) {
        instances.add(this);

        this.content = content;
    }

    public String[] generateContent(McsuPlayer player) {
        return content;
    }

}
