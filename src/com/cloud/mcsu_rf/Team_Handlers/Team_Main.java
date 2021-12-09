package com.cloud.mcsu_rf.Team_Handlers;

import com.cloud.mcsu_rf.Objects.MCSU_Team;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class Team_Main {

    public static ArrayList<MCSU_Team> Teams = new ArrayList();

    static void initTeams() {
        Teams.add(new MCSU_Team("Blue smurfs haha", "b", ChatColor.BLUE));
    }

    public static void init() {
        initTeams();
        Scoreboard_Main.onTeamsLoaded();
    }

    public static MCSU_Team getTeamById(String ID) {
        return Teams.stream().filter(Team -> Team.TeamID == ID).findFirst().orElse(null);
    }

    public static ArrayList<MCSU_Team> getSortedTeams() {
        ArrayList<MCSU_Team> sortedTeams = Teams;

        sortedTeams.sort((t1, t2) -> {
            if (t1.Points > t2.Points)
                return 1;
            if (t1.Points < t2.Points)
                return -1;
            return 0;
        });

        return sortedTeams;
    }

}
