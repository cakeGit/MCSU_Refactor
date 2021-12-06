package com.cloud.mcsu_rf.Team_Handlers;

import com.cloud.mcsu_rf.Objects.Team;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class Team_Main {

    public static ArrayList<Team> Teams = new ArrayList<Team>();

    static void initTeams() {
        Teams.add(new Team("Blue smurfs haha", ChatColor.BLUE));
    }

    public static void init() {
        initTeams();
        Scoreboard_Main.onTeamsLoaded();
    }

    public static ArrayList<Team> getSortedTeams() {
        ArrayList<Team> sortedTeams = Teams;

        sortedTeams.sort((t1, t2) -> {
            if (t1.getPoints() > t2.getPoints())
                return 1;
            if (t1.getPoints() < t2.getPoints())
                return -1;
            return 0;
        });

        return sortedTeams;
    }

}
