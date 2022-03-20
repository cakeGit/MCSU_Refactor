package com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElements;

import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Lambdas.TeamLambda;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElementBase;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TeamTotalPoints extends ScoreboardElementBase {

    void topTeamsLoop(int maxTeams, TeamLambda lambda) {
        int i=0;
        for ( McsuTeam team : TeamMain.getSortedTeams() ) {
            if (i<maxTeams) lambda.exec(team); i++;
        }
    }

    public String[] generateContent() {

        ArrayList<String> content = new ArrayList<>();
        if (Game.gameActive) {
            topTeamsLoop(4, team -> content.add(team.toScoreboardString()));
        } else {
            topTeamsLoop(8, team -> content.add(team.toScoreboardString()));
        }

        return content.toArray( new String[]{} );

    }

}
