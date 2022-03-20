package com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElements;

import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElementBase;

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
